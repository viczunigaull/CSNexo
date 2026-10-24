# Informe de desarrollo — Primera Entrega

**Proyecto:** CSNexo — Gestión móvil de oportunidades y activaciones corporativas  
**Caso:** Caso_DSY1105_003D  
**Asignatura:** [completar]  
**Integrantes:** [completar nombre y apellido de cada integrante]  
**Fecha:** [completar]  
**Repositorio:** https://github.com/viczunigaull/CSNexo  
**Rama de trabajo:** `Vicente`

> Este informe documenta únicamente el alcance de la **primera entrega**: autenticación, menú y módulo de Oportunidades. Los módulos de eventos, organizaciones, contactos, seguimiento, alertas, filtros e historial pertenecen al alcance transversal y no se incluyen aquí.

---

## 1. Resumen del avance

La aplicación permite iniciar sesión con usuarios ficticios, acceder a un menú, listar las oportunidades guardadas en el dispositivo, crear nuevas oportunidades, editarlas y adjuntar evidencia fotográfica y una referencia de ubicación.

El flujo principal implementado es:

```text
Login → Menú → Lista de oportunidades → Formulario → Guardar → Lista actualizada
```

La información se conserva al cerrar y volver a abrir la aplicación porque se almacena en **Room**. La sesión se conserva con **DataStore**.

**Estado de compilación:** `assembleDebug` finaliza correctamente.  
**Pruebas unitarias/instrumentadas:** no se agregaron, siguiendo la indicación de la pauta de no modificar `test` ni `androidTest`.

---

## 2. Alcance implementado

| Funcionalidad | Estado |
|---|---|
| Login con usuarios ficticios | Implementado |
| Sesión persistente | Implementado con DataStore |
| Menú principal | Implementado |
| Listado de oportunidades | Implementado |
| Crear oportunidad | Implementado |
| Editar oportunidad | Implementado |
| Validaciones por campo | Implementado |
| Persistencia local | Implementado con Room |
| Lista reactiva | Implementado con `Flow` |
| Cámara para evidencia | Implementado con CameraX |
| Ubicación de referencia puntual | Implementado con `LocationManager` |
| Eventos, contactos, seguimiento, alertas, filtros e historial | Fuera de esta entrega |

Todos los datos usados son ficticios. No se utilizan RUT, correos, teléfonos ni credenciales reales.

---

## 3. Arquitectura

Se aplicó MVVM con separación entre:

- **View:** pantallas Compose y componentes Material 3.
- **ViewModel:** estado de pantalla, validaciones y coordinación de acciones.
- **Model:** modelos de dominio, repositorios, entidades Room, DAO y mappers.

El flujo de datos es unidireccional:

```text
Compose → ViewModel → Repository → Room
Compose ← StateFlow ← ViewModel ← Flow de Room
```

Decisiones principales:

- Los composables no acceden a Room ni a DataStore.
- Los ViewModels no conocen DAOs ni entidades Room.
- Los repositorios no conocen pantallas ni ViewModels.
- La sesión se guarda en DataStore; las oportunidades, en Room.
- La inyección de dependencias es manual, mediante `AppContainer` y `AppViewModelProvider`.
- Se usan dos ViewModels para oportunidades: uno para la lista y otro para el formulario.
- La cámara y la ubicación se encapsulan en componentes de UI y métodos del ViewModel; no se agregó una capa extra de casos de uso.

### Estructura de paquetes

```text
com.example.csnexo
├── data
│   ├── local
│   │   ├── dao
│   │   ├── entity
│   │   └── seed
│   ├── mapper
│   ├── preferences
│   └── repository
├── di
├── domain
│   ├── model
│   └── repository
├── navigation
└── ui
    ├── auth
    ├── menu
    ├── oportunidades
    │   ├── form
    │   └── list
    └── theme
```

---

## 4. Hitos de desarrollo

### Hito 1. Estructura del proyecto y configuración

**Descripción:** se organizó el proyecto Android en paquetes por responsabilidad (`data`, `domain`, `ui`, `di`, `navigation`) y se configuraron Kotlin, Compose, Material 3, Navigation, Room, DataStore, CameraX y KSP.

**Evidencia visual:**

![Captura pendiente: estructura de paquetes](evidencias/01-estructura-paquetes.png)

- Captura de Android Studio con el árbol de paquetes.
- Captura del archivo `app/build.gradle.kts` o del catálogo de versiones.

**Resultado obtenido:** el proyecto compila con `assembleDebug` y las dependencias quedan centralizadas en `gradle/libs.versions.toml`.

---

### Hito 2. Arquitectura MVVM y separación de responsabilidades

**Descripción:** se separó la pantalla, el estado y el acceso a datos. La lista usa `ListaOportunidadesViewModel`; el formulario usa `OportunidadFormViewModel`; ambos consumen `OportunidadRepository` y no conocen Room.

**Evidencia visual:**

![Captura pendiente: ViewModel y repositorio](evidencias/02-mvvm-viewmodel-repositorio.png)

- Captura de `ListaOportunidadesViewModel.kt`.
- Captura de `OportunidadRepository.kt` y `OportunidadRepositoryImpl.kt`.

**Resultado obtenido:** los composables reciben estado y callbacks; los ViewModels no importan DAOs ni entidades Room.

---

### Hito 3. Navegación Login → Menú → Lista → Formulario

**Descripción:** se implementó la navegación con Navigation Compose. El login queda fuera del grafo autenticado; al iniciar sesión se muestra el menú y desde allí se accede a la lista y al formulario.

**Evidencia visual:**

![Captura pendiente: navegación](evidencias/03-navegacion.png)

- Captura de `Routes.kt` y `CSNexoNavHost.kt`.
- Captura de la secuencia Login → Menú → Lista.

**Resultado obtenido:** el usuario puede recorrer el flujo sin retroceder a pantallas autenticadas después de cerrar sesión.

---

### Hito 4. Persistencia local con Room y listado reactivo

**Descripción:** se creó `AppDatabase`, la entidad `OportunidadEntity`, el DAO y el repositorio. La lista observa un `Flow` de Room, por lo que se actualiza cuando se inserta o modifica una oportunidad.

**Evidencia visual:**

![Captura pendiente: base de datos Room](evidencias/04-room-base-datos.png)

- Captura de `OportunidadEntity.kt`, `OportunidadDao.kt` y `AppDatabase.kt`.
- Captura de la lista con las oportunidades sembradas.
- Captura de la app después de reiniciarla, mostrando que los datos siguen disponibles.

**Resultado obtenido:** los datos no se guardan en una lista en memoria; permanecen después de cerrar y abrir la aplicación.

---

### Hito 5. Formulario de creación y edición con validaciones

**Descripción:** el formulario permite crear y editar. Valida nombre, tipo, descripción, fuente, prioridad y potencial comercial. Los errores se muestran debajo de cada campo y el guardado se bloquea mientras hay errores.

**Evidencia visual:**

![Captura pendiente: errores de validación](evidencias/05-validaciones.png)

- Captura del formulario vacío con errores visibles.
- Captura del formulario completo antes de guardar.
- Captura del formulario en modo edición con datos precargados.

**Resultado obtenido:** los errores se controlan desde el ViewModel y se muestran en la interfaz; una oportunidad válida se guarda en Room.

---

### Hito 6. Gestión de estado con UiState y StateFlow

**Descripción:** se usan estados inmutables (`OportunidadFormUiState`, `ListaOportunidadesUiState`) y `StateFlow` de solo lectura. La UI recolecta el estado con `collectAsStateWithLifecycle`.

**Evidencia visual:**

![Captura pendiente: UiState y StateFlow](evidencias/06-uistate-stateflow.png)

- Captura de `OportunidadFormUiState.kt`.
- Captura de `ListaOportunidadesViewModel.kt` con `stateIn`.
- Captura del formulario mostrando `guardando` o errores.

**Resultado obtenido:** la pantalla reacciona a cambios de estado sin que el composable modifique directamente la base de datos.

---

### Hito 7. Recurso nativo: cámara y ubicación de referencia

**Descripción:** se integró CameraX para capturar una fotografía y guardarla en el almacenamiento interno de la app. También se agregó una consulta puntual de ubicación como referencia de prueba. Ambos datos son opcionales y quedan asociados a la oportunidad.

**Evidencia visual:**

![Captura pendiente: cámara](evidencias/07-camara.png)

- Captura del diálogo de permiso de cámara.
- Captura de la vista previa y la foto capturada.
- Captura de la foto visible en el formulario.

![Captura pendiente: ubicación](evidencias/07-ubicacion.png)

- Captura del permiso de ubicación.
- Captura de la referencia de ubicación en el formulario.

**Resultado obtenido:** la foto y la ubicación se guardan con la oportunidad y se recuperan al volver a editarla. Si se niega el permiso, la app informa y permite continuar sin esos datos.

---

### Hito 8. Funcionamiento integral y evidencias de cierre

**Descripción:** se verificó el flujo completo: login, menú, listado, creación, edición, cámara, ubicación, guardado y persistencia después de reiniciar.

**Evidencia visual:**

![Captura pendiente: flujo completo](evidencias/08-flujo-completo.png)

- Captura de la lista antes y después de crear una oportunidad.
- Captura de la oportunidad editada.
- Captura de la app después de reiniciarla.
- Captura de `git log --oneline` con los commits del equipo.

**Resultado obtenido:** el flujo mínimo de la primera entrega queda operativo y los datos persisten localmente.

---

## 5. Checklist de capturas

Guardar las imágenes en `docs/evidencias/` con estos nombres:

- [ ] `01-estructura-paquetes.png`
- [ ] `02-mvvm-viewmodel-repositorio.png`
- [ ] `03-navegacion.png`
- [ ] `04-room-base-datos.png`
- [ ] `05-validaciones.png`
- [ ] `06-uistate-stateflow.png`
- [ ] `07-camara.png`
- [ ] `07-ubicacion.png`
- [ ] `08-flujo-completo.png`

Cada captura debe ser legible y mostrar la pantalla o el código real del proyecto. No se deben usar imágenes de otros proyectos ni datos reales.

---

## 6. Prueba manual sugerida

| Paso | Acción | Resultado esperado |
|---|---|---|
| 1 | Abrir la app | Se muestra el login |
| 2 | Ingresar `ejecutivo.demo` / `demo1234` | Se muestra el menú |
| 3 | Abrir Oportunidades | Se ven las oportunidades sembradas |
| 4 | Tocar `+` y guardar vacío | Aparecen errores por campo |
| 5 | Completar los campos y guardar | Se vuelve a la lista y aparece la oportunidad |
| 6 | Abrir la oportunidad creada | El formulario se carga con sus datos |
| 7 | Cambiar un dato y guardar | La lista refleja el cambio |
| 8 | Tomar una foto y usar ubicación | Se muestran la foto y la referencia |
| 9 | Cerrar y abrir la app | La oportunidad y la evidencia siguen disponibles |
| 10 | Cerrar sesión | Se vuelve al login |

---

## 7. Limitaciones y decisiones

- La primera entrega implementa **solo Oportunidades**; el resto del caso queda para el alcance transversal.
- No se implementó backend ni sincronización remota.
- No se implementaron alertas automáticas ni notificaciones.
- No se implementaron filtros ni historial de cambios.
- No se agregaron pruebas unitarias ni instrumentadas, según la indicación recibida.
- La foto se guarda en el almacenamiento interno de la app; no se comparte con terceros.
- La ubicación se consulta una sola vez como referencia de prueba; no hay seguimiento ni mapas.
- Para mostrar la foto se usa `BitmapFactory` con reducción de resolución, evitando una dependencia adicional que no fuera compatible con la versión de Kotlin del proyecto.

---

## 8. Conclusión

La primera entrega cumple el flujo mínimo solicitado: autenticación ficticia, menú, listado de oportunidades, creación, edición, validaciones, persistencia local y un recurso nativo integrado. La arquitectura separa claramente View, ViewModel, repositorio y fuentes de datos, y la lista se mantiene sincronizada con Room.

Las capturas de ejecución deben agregarse en `docs/evidencias/` antes de la entrega formal.
