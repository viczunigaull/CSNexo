# Git — Continuidad en el repositorio CSNexo

El proyecto ya quedó en un repositorio git **local**, con un commit inicial y el
remote `origin` configurado. Este archivo resume cómo subirlo a GitHub **sin
perder la continuidad** con tu repositorio existente.

## Estado actual

```text
rama: main (o Vicente)
commit: "Integra microservicio Spring Boot, Retrofit, API externa, pruebas y firma del APK"
remote origin: https://github.com/viczunigaull/CSNexo.git
```

> ⚠️ Ajusta la URL del remote y el nombre de rama a tu repositorio real si
> difieren. Verifica con: `git remote -v`

## Caso A — Tu repo de GitHub ya tiene historial (rama `Vicente`)

```powershell
git remote set-url origin https://github.com/TU_USUARIO/CSNexo.git
git pull origin Vicente --allow-unrelated-histories
# si aparecen conflictos, resuélvelos y luego:
git push origin Vicente
```

## Caso B — Quieres subir este trabajo como la rama principal

```powershell
git remote set-url origin https://github.com/TU_USUARIO/CSNexo.git
git branch -M main
git push -u origin main
```

## Flujo de trabajo recomendado (para la defensa)

```powershell
# trabajar en una rama de feature
git checkout -b feature/guia11-formulario-resumen

# commits pequeños y descriptivos (la pauta evalúa commits por integrante)
git add -A
git commit -m "Formulario y resumen funcional"

# subir la rama
git push -u origin feature/guia11-formulario-resumen
```

## Ajustar identidad

```powershell
git config user.name "Tu Nombre"
git config user.email "tu@correo.com"
```

## Nota sobre el keystore

`app/csnexo-release.jks` está commiteado (la pauta pide dejar el `.jks` y el APK
firmado en el repositorio). Sus contraseñas son solo para el curso y están en
`app/build.gradle.kts` (`signingConfigs`).
