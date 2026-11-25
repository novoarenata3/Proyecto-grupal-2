# Sistema de inventario (Smart Inventory)

API en Spring Boot para gestionar productos y movimientos de stock de una cafetería, restaurante o tienda.

## Descripción

- **Empleado:** registra entradas y salidas, consulta existencias, alertas e historial.
- **Administrador:** tiene los permisos del empleado y además gestiona productos, cuentas de empleados y ajustes de inventario.
- **Alertas:** cuando el stock de un producto es igual o inferior a su mínimo se muestra una alerta y se sugiere comprar la diferencia entre la cantidad objetivo y el stock disponible.

## Tecnologías

Java 17, Spring Boot 3, Spring Security, PostgreSQL 16, Flyway y Docker. Detalle en [`docs/stack-tecnologico.md`](docs/stack-tecnologico.md).

## Cómo levantar y probar el proyecto con Docker

Necesitas Git y Docker Desktop en ejecución. La primera compilación puede descargar imágenes y dependencias de Maven Central.

1. Clona el repositorio y entra en la carpeta.

   ```text
   git clone https://github.com/novoarenata3/Proyecto-grupal-2.git
   cd Proyecto-grupal-2
   ```

2. Crea `.env` desde la plantilla. En PowerShell usa `Copy-Item .env.example .env`. En macOS o Linux usa `cp .env.example .env`. Si ya tienes un `.env`, conserva el archivo existente.

   En una instalación nueva, sustituye los valores de ejemplo de `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, `ADMIN_USERNAME` y `ADMIN_PASSWORD`. Usa contraseñas largas y distintas. Mantén `.env` fuera de Git.

3. Valida Compose y levanta los servicios.

   ```text
   docker compose config --quiet
   docker compose up -d --build
   docker compose ps
   ```

   La validación termina en silencio si la configuración es correcta. En `ps`, espera a que `db` esté `healthy` y `backend` esté `Up`. Si la API todavía no responde, usa `docker compose logs --tail 80 backend` y busca `Started SmartInventoryApplication`.

4. Prueba el acceso con el usuario de `ADMIN_USERNAME`. Sustituye `TU_USUARIO_ADMIN` y escribe tu contraseña cuando se te pida. En macOS o Linux, usa `curl` en vez de `curl.exe`.

   ```text
   curl.exe -i -u TU_USUARIO_ADMIN http://localhost:8080/api/me
   curl.exe -i -u TU_USUARIO_ADMIN http://localhost:8080/api/productos
   ```

   Espera `200` con el rol `ADMIN` en `/api/me` y `200` con `[]` en `/api/productos` si la base es nueva. En Postman, usa **Basic Auth** con las mismas credenciales. Una petición del navegador sin credenciales recibe `401`.

5. Para detener los servicios, ejecuta `docker compose down`. La opción `-v` elimina también el volumen de PostgreSQL y sus datos.

Si los puertos `8080` o `5432` ya están ocupados, revisa si hay otra copia del proyecto en ejecución. La cuenta de administrador de `.env` se crea cuando la base está vacía. En una base existente se conservan las cuentas y contraseñas guardadas.

## Estado de los módulos

| Módulo | Estado |
|---|---|
| Usuarios, login, roles y empleados (`usuarios/`, `config/`, `common/`) | Implementado |
| Docker y guía de arranque | Implementado |
| Productos y alertas (`productos/`, `alertas/`) | Implementado |
| Movimientos, ajustes e historial (`movimientos/`) | Implementado |
| Frontend: 5 pantallas (`src/main/resources/static/`) | Pendiente |

Las rutas que debe exponer cada módulo están en [`docs/api.md`](docs/api.md).

## Cómo agregar tu módulo

1. Actualiza `main` y crea tu rama: `git pull origin main` y luego `git checkout -b feature/<tu-modulo>`.
2. Trabaja solo en tu paquete dentro de `src/main/java/com/smartinventory/`.
3. Para cambios en la base de datos, crea una nueva migración con el siguiente número disponible en `src/main/resources/db/migration/`.
4. Los permisos por rol ya están definidos en `config/SecurityConfig.java`.
5. Sube tu rama y abre un pull request; otra persona lo revisa antes de unirlo a `main`.

## Estructura del repositorio

```
.
├── pom.xml
├── src/main/java/com/smartinventory/
│   ├── usuarios/       # Login, roles, cuenta inicial y gestión de empleados
│   ├── config/         # Seguridad
│   ├── common/         # Manejo de errores
│   ├── productos/      # Catálogo de inventario
│   ├── alertas/        # Consultas de reposición
│   └── movimientos/    # Entradas, salidas, ajustes e historial
├── src/main/resources/
│   ├── application.properties
│   └── db/migration/   # Migraciones de la base de datos (Flyway)
├── docker-compose.yml
├── docker/backend.Dockerfile
├── .env.example
└── docs/               # Arquitectura, API, pruebas, bitácora, trazabilidad y sprints
```

## Flujo de trabajo

- Una rama por tarea: `feature/<módulo>-<descripción>`, `docs/<tema>` o `fix/<descripción>`.
- Nadie hace push directo a `main`: todo entra por pull request revisado por otra persona.
- Detalle en [`docs/versionamiento.md`](docs/versionamiento.md).

## Equipo

| Integrante | Nombre | Rol | Entregables |
|---|---|---|---|
| 1 | Alison | Gestión y documentación | Notion, backlog, historias de usuario, kanban, actas y resumen de aportes |
| 2 | Marco | Backend | Lógica del inventario, base de datos y Dockerfile del backend |
| 3 | Renata | Frontend | Pantallas, conexión con la API y Dockerfile del frontend |
| 4 | Antonio | DevOps e integraciones | GitHub (ramas y pull requests), Notion ↔ GitHub ↔ Discord, Docker Compose y README de arranque |
| 5 | Alison | QA y arquitectura | Pruebas funcionales, bitácora de cambios, arquitectura y stack tecnológico |
