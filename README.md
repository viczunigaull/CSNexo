# CSNexo - Gestion Movil de Oportunidades y Activaciones Corporativas

Aplicacion movil desarrollada en Android con Kotlin y Jetpack Compose para centralizar, organizar y priorizar la gestion de oportunidades comerciales, eventos y convenios corporativos en terreno

Proyecto desarrollado para la Evaluacion Parcial 2 (Encargo) de la asignatura Desarrollo de Aplicaciones Moviles (DSY1105) en Duoc UC

---

## Integrantes
* Francisco Moreno
* Vicente Zuñiga

* Docente: Pablo Saldaña
* Asignatura: Desarrollo de Aplicaciones Moviles (DSY1105)
* Sede: Duoc UC Puente Alto
* Fecha: Octubre 2026

---

## Descripcion del Proyecto
CSNexo soluciona la dispersion de informacion en la busqueda de eventos corporativos (ferias, actividades municipales y jornadas de beneficios), permitiendo al equipo comercial registrar antecedentes, definir prioridades y dar seguimiento en una base de datos local y centralizada

---

## Funcionalidades Implementadas

* Autenticacion y Roles: Inicio de sesion simulado con perfiles corporativos (Admin, Ejecutivo, Supervisor) y persistencia con Jetpack DataStore
* Gestion de Oportunidades: Listado y registro de oportunidades clasificadas por tipo, nivel de prioridad, potencial comercial y estado de avance
* Formularios Validados: Validaciones gestionadas en el ViewModel con retroalimentacion visual de errores en pantalla
* Persistencia Local: Base de datos SQLite gestionada con Room (AppDatabase, DAOs, Entities) y precarga automatica de datos simulados (Seeder)
* Recursos Nativos (2 recursos): Integracion de camara nativa para captura de fotografias y selector de archivos/galeria multimedia para asociar evidencias a la oportunidad
* Navegacion y UI: Pantallas construidas con Material Design 3, transiciones de pantalla y modales de interaccion

---

## Arquitectura y Tecnologias

* Lenguaje: Kotlin
* UI: Jetpack Compose y Material 3
* Patron Arquitectonico: MVVM (Model - View - ViewModel) con patron Repository
* Base de Datos Local: Room Database
* Preferencias: Jetpack DataStore
* Concurrencia: Kotlin Coroutines y Flow
* Control de versiones y planificacion: Git, GitHub y Trello

---

## Estructura del Codigo

* data: Base de datos Room (entidades, DAOs, seeder), DataStore y repositorios locales
* domain: Modelos de negocio (Oportunidad, Usuario, Estados, Prioridades) e interfaces de repositorios
* di: Contenedor manual de dependencias de la aplicacion (AppContainer)
* navigation: Control de rutas y NavHost con Compose Navigation.
* ui: Vistas, ViewModels y componentes de Compose organizados por modulo (auth, menu, oportunidades).

---

## Credenciales de Prueba (Datos Ficticios)

| Usuario | Contrasena | Rol |
| :--- | :--- | :--- |
| admin@csnexo.cl | admin123 | Administrador |
| ejecutivo@csnexo.cl | ejecutivo123 | Ejecutivo Comercial |
| supervisor@csnexo.cl | super123 | Supervisor |

Todos los nombres, contactos y empresas corresponden exclusivamente a informacion ficticia y simulada para fines academicos

---

## Requisitos y Pasos para Ejecutar

1. Requisitos:
   * Android Studio Ladybug o superior.
   * Android SDK 34 o 35 instalado.
   * JDK 17 o JDK 21 configurado en Gradle JDK.
2. Clonar el repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
