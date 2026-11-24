# Bitácora de cambios

Aquí se anota cada problema encontrado y qué se cambió para que funcione. Se actualiza durante todo el proyecto, no solo al final.

| Fecha | Qué falló o qué decisión se tomó | Qué se cambió | Responsable | PR o commit |
|---|---|---|---|---|
| 19/11/2026 | El repositorio solo tenía el README inicial y no había estructura de carpetas | Se agrega la estructura `docs/`, `docker/` y `.github/` | Alison | PR de documentación del Sprint 1 |
| 23/11/2026 | El quinto integrante no se unió al repositorio y el rol A quedó sin responsable | Alison asume el rol A además del E e implementa el esqueleto, la seguridad y la gestión de empleados | Alison | Pendiente de PR |
| 23/11/2026 | Decisión de seguridad: JWT o HTTP Basic | Se usa HTTP Basic con BCrypt por simplicidad y tiempo | Alison | Pendiente de PR |
| 23/11/2026 | Para no editar los mismos archivos, cada módulo necesita su propia migración de base de datos | Se reserva `V1__usuarios` (A), `V2__productos` (B) y `V3__movimientos` (C) | Alison | Pendiente de PR |
| 23/11/2026 | El `.env.example` inicial tenía otra contraseña de base de datos que la configuración de Spring | Se unifica la contraseña por defecto en `.env.example` y en `application.properties` | Alison | Pendiente de PR |
| 24/11/2026 | .env.example y .gitignore se subieron con BOM y el .gitignore no ignoraba target/ | Se guardan de nuevo sin BOM | Alison | PR de corrección |