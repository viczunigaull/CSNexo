# Evidencias de la primera entrega

Guardar aquí las capturas reales de la aplicación y del código. Los nombres deben coincidir con los usados en `docs/informe-entrega-1.md`.

## Capturas necesarias

| Archivo | Qué debe mostrar |
|---|---|
| `01-estructura-paquetes.png` | Árbol de paquetes del proyecto en Android Studio |
| `02-mvvm-viewmodel-repositorio.png` | ViewModel y repositorio, sin DAOs en la UI |
| `03-navegacion.png` | Rutas y grafo de navegación, o la secuencia Login → Menú → Lista |
| `04-room-base-datos.png` | Entidad, DAO o Base de datos, y la lista con datos |
| `05-validaciones.png` | Formulario vacío con errores visibles |
| `06-uistate-stateflow.png` | UiState y StateFlow en el código |
| `07-camara.png` | Permiso, vista previa y foto en el formulario |
| `07-ubicacion.png` | Permiso y referencia de ubicación en el formulario |
| `08-flujo-completo.png` | Lista después de crear/editar y app después de reiniciar |

## Cómo tomarlas

1. Ejecutar la app en un emulador o dispositivo.
2. Iniciar sesión con `ejecutivo.demo` / `demo1234`.
3. Seguir el flujo de la tabla y tomar la captura con la herramienta del emulador o del sistema.
4. Guardar cada imagen en esta carpeta con el nombre exacto.
5. Verificar que no aparezcan datos reales, correos reales ni credenciales reales.

## Captura rápida con adb

Con un dispositivo o emulador conectado:

```powershell
adb exec-out screencap -p > docs/evidencias/03-navegacion.png
```

Cambiar el nombre del archivo según la captura. También se puede usar la cámara del emulador para la foto y configurar una ubicación de prueba desde sus controles.

## Antes de entregar

- [ ] Las 9 imágenes existen y se abren correctamente.
- [ ] El informe menciona cada imagen.
- [ ] Las capturas corresponden a este proyecto.
- [ ] Los datos mostrados son ficticios.
- [ ] El informe tiene los nombres de los integrantes.
