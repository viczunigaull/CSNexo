# CSNexo — Gestión móvil de oportunidades y activaciones corporativas

**Caso:** Caso_DSY1105_003D
**Asignatura:** DSY1105 · Desarrollo de Aplicaciones Móviles
**Integrantes:** `[completar nombre y apellido de cada integrante]`
**Repositorio:** `https://github.com/.../CSNexo`

Aplicación móvil (**Kotlin + Jetpack Compose**) y microservicio (**Spring Boot**) para
centralizar, priorizar y dar seguimiento a oportunidades de activación corporativa
(ferias, eventos, jornadas de beneficios, etc.). Todos los datos son **ficticios**.

---

## 1. Qué hace la aplicación

| Funcionalidad | Cómo se implementa |
|---|---|
| Login con usuarios ficticios | `FakeAuthRepository` + sesión en `DataStore` |
| Menú principal | Navigation Compose |
| Listar oportunidades | `Flow` de Room (reactivo) |
| Crear / editar / eliminar oportunidad | `OportunidadFormViewModel` + validaciones |
| Persistencia local (offline) | Room (SQLite) |
| **Microservicio propio** | Spring Boot + H2 (CRUD REST) |
| **Sincronizar con el servidor** | Retrofit + `OportunidadServidor` |
| **API externa** (feriados) | Retrofit (`date.nager.at`) |
| Evidencia fotográfica | CameraX |
| Ubicación de referencia | `LocationManager` (consulta puntual) |

---

## 2. Arquitectura (MVVM)

```
┌────────────────────────────────────────────────────────────┐
│                        UI (Compose)                        │
│  Login · Menú · Lista · Formulario · Feriados              │
│  Solo recibe estado (UiState) y emite eventos (callbacks)  │
└───────────────────────────┬────────────────────────────────┘
                            │ StateFlow / callbacks
┌───────────────────────────▼────────────────────────────────┐
│                      ViewModel                             │
│  Valida, coordina, transforma datos. No conoce Room/Retrofit│
└───────┬───────────────────────────────┬────────────────────┘
        │ domain.repository (interfaces) │
┌───────▼───────────────┐   ┌───────────▼────────────────────┐
│  Room (local)         │   │  Retrofit (remoto)             │
│  DAO → Entity → Mapper│   │  Api → DTO → Mapper            │
│  → Oportunidad (dom)  │   │  → OportunidadServidor         │
└───────────────────────┘   └───────────┬────────────────────┘
                                        │ HTTP
                             ┌──────────▼──────────┐
                             │ Spring Boot (H2)    │
                             │ Controller → JPA    │
                             └─────────────────────┘
```

**Reglas de oro (para explicar en la defensa):**
1. Los composables **no** tocan Room ni Retrofit.
2. Los ViewModels **no** conocen DAOs, entidades ni DTOs; solo usan interfaces y `Result`.
3. La inyección de dependencias es manual (`AppContainer` + `AppViewModelProvider`).
4. Los mappers convierten Entity/DTO ⇄ modelo de dominio.

**"Pasado de información" (Guía 11):** el formulario guarda y navega a la pantalla
**Resumen** pasando el ID por la ruta; `ResumenViewModel` lo lee con `SavedStateHandle`
y lo carga del repositorio. Es la misma técnica que usa la edición.

---

## 3. Mapa del proyecto (dónde está cada cosa)

### App Android (`CSNexo-Vicente/app/src/main/java/com/example/csnexo/`)

```
csnexo/
├── CSNexoApplication.kt        # Crea el AppContainer (una sola vez)
├── MainActivity.kt             # Punto de entrada, setContent + tema
├── data/
│   ├── local/                  # Room: AppDatabase, dao/, entity/, seed/
│   ├── mapper/OportunidadMapper.kt      # Entity ⇄ dominio
│   ├── preferences/SesionDataStore.kt   # Sesión (DataStore)
│   ├── remote/                 # Retrofit: DTOs, Apis, Servidor, RetrofitClient
│   └── repository/             # Implementaciones de los repositorios
├── di/                         # AppContainer + AppViewModelProvider
├── domain/
│   ├── model/                  # Oportunidad, Usuario, enums...
│   └── repository/             # Interfaces (contratos)
├── navigation/                 # Routes + CSNexoNavHost
└── ui/
    ├── CSNexoApp.kt            # Decide login vs. navegación
    ├── auth/                   # LoginScreen + LoginViewModel + SesionViewModel
    ├── menu/                   # MenuScreen
    ├── componentes/            # CampoTexto, Selector, Tarjeta, MensajeError
    ├── oportunidades/
    │   ├── list/               # Lista + ListaOportunidadesViewModel
    │   ├── form/               # Formulario + OportunidadFormViewModel
    │   └── resumen/            # Resumen (detalle) + ResumenViewModel
    ├── organizaciones/
    │   ├── list/               # Lista + OrganizacionListaViewModel
    │   └── form/               # Formulario + OrganizacionFormViewModel
    ├── eventos/
    │   ├── list/               # Lista + EventoListaViewModel
    │   └── form/               # Formulario + EventoFormViewModel
    ├── contactos/
    │   ├── list/               # Lista + ContactoListaViewModel
    │   └── form/               # Formulario + ContactoFormViewModel
    ├── seguimientos/
    │   ├── list/               # Lista + SeguimientoListaViewModel
    │   └── form/               # Formulario + SeguimientoFormViewModel
    ├── alertas/                # Resumen (eventos próximos + seguimientos)
    ├── feriados/               # API externa (FeriadosViewModel)
    └── theme/                  # Color, Theme, Type
```

### Microservicio (`backend/src/main/kotlin/com/example/csnexo/backend/`)

```
backend/
├── CsnexoBackendApplication.kt  # main() → levanta Spring Boot en :8080
├── Oportunidad.kt               # Entidad JPA (tabla "oportunidades")
├── OportunidadRepository.kt     # Spring Data JPA (CRUD automático)
├── OportunidadController.kt     # Endpoints REST /api/oportunidades
└── DatosIniciales.kt            # Siembra 2 oportunidades ficticias
```

---

## 4. Cómo ejecutar

### 4.1 Levantar el microservicio (Spring Boot)

```powershell
cd backend
.\gradlew.bat bootRun
```

Queda escuchando en `http://localhost:8080`. La base H2 está en memoria
(se reinicia al apagar). Consola H2: `http://localhost:8080/h2-console`
(JDBC URL: `jdbc:h2:mem:csnexo`, user `sa`, sin clave).

### 4.2 Ejecutar la app

1. Abrir `CSNexo-Vicente` en Android Studio.
2. Ejecutar en un emulador.
3. Usuario de prueba: `ejecutivo.demo` / `demo1234` (o `supervisor.demo`).

> El emulador llega al computador por la IP `10.0.2.2` (ya configurada en
> `RetrofitClient`). Para un **dispositivo físico**, cambia esa URL por la IP
> LAN del computador.

---

## 5. Endpoints del microservicio

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/oportunidades` | Listar todas |
| GET | `/api/oportunidades/{id}` | Obtener una |
| POST | `/api/oportunidades` | Crear |
| PUT | `/api/oportunidades/{id}` | Actualizar |
| DELETE | `/api/oportunidades/{id}` | Eliminar |
| GET/POST/PUT/DELETE | `/api/organizaciones` | CRUD de organizaciones |
| GET/POST/PUT/DELETE | `/api/eventos` | CRUD de eventos |
| GET/POST/PUT/DELETE | `/api/contactos` | CRUD de contactos |
| GET/POST/PUT/DELETE | `/api/seguimientos` | CRUD de seguimientos |

---

## 6. API externa

Se consume `https://date.nager.at/api/v3/PublicHolidays/{anio}/CL` (feriados de
Chile, sin necesidad de clave). Se muestra en la pantalla **Feriados**.

---

## 7. Pruebas unitarias

```powershell
cd CSNexo-Vicente
.\gradlew.bat testDebugUnitTest
```

Se prueban los mappers (Entity/DTO ⇄ dominio) y los ViewModels (login,
formulario de oportunidades y feriados) usando JUnit + `kotlinx-coroutines-test`
y fakes (no se necesita dispositivo).

---

## 8. APK firmado

```powershell
cd CSNexo-Vicente
.\gradlew.bat assembleRelease
```

- Salida: `app/build/outputs/apk/release/app-release.apk`
- Keystore: `app/csnexo-release.jks`
- Alias: `csnexo` · contraseñas: `csnexo123` (solo para el curso; no usar en producción)
- La configuración está en `app/build.gradle.kts` → `signingConfigs`.

---

## 9. Tecnologías

Kotlin · Android Studio · Jetpack Compose · Material 3 · MVVM · Room (SQLite) ·
DataStore · Navigation Compose · CameraX · Retrofit + Gson · Spring Boot ·
Spring Data JPA · H2 · JUnit · Git/GitHub · Trello · APK firmado (release).

---

## 10. Estado y próximos pasos

**Implementado:** autenticación, menú, CRUD completo de oportunidades,
organizaciones, eventos, contactos y seguimientos (local + servidor), resumen,
alertas, filtros, API externa (feriados), pruebas unitarias, APK firmado.

**Nota:** el "historial" de una oportunidad se cubre con el módulo Seguimiento
(acciones registradas por fecha).
