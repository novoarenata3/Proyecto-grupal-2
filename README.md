# Sistema de inventario (Smart Inventory)

API en Spring Boot para gestionar productos y movimientos de stock de una cafetería, restaurante o tienda.

## Descripción

- **Empleado:** registra entradas y salidas, consulta existencias, alertas e historial.
- **Administrador:** tiene los permisos del empleado y además gestiona productos, cuentas de empleados y ajustes de inventario.
- **Alertas:** cuando el stock de un producto es igual o inferior a su mínimo se muestra una alerta y se sugiere comprar la diferencia entre la cantidad objetivo y el stock disponible.

## Tecnologías

Java 17, Spring Boot 3, Spring Security, PostgreSQL 16, Flyway y Docker. Detalle en [`docs/stack-tecnologico.md`](docs/stack-tecnologico.md).

## Cómo levantar el proyecto (con Docker)

Requisito: Docker Desktop instalado y en ejecución.

1. Copia las variables de ejemplo: `copy .env.example .env` (PowerShell) o `cp .env.example .env`. Edita `.env` y reemplaza todos los valores de ejemplo por valores propios y contraseñas largas y únicas. No compartas ni subas `.env` a Git.
2. Construye y levanta todo: `docker compose up -d --build` (la primera vez tarda unos minutos). Compose falla si falta alguna variable requerida.
3. Revisa que ambos servicios estén arriba: `docker compose ps`.
4. Prueba el login con la cuenta inicial de administrador, usando `ADMIN_USERNAME` y `ADMIN_PASSWORD` configurados en `.env` (Postman, autenticación *Basic Auth*).
5. Registros: `docker compose logs -f backend`. Apagar: `docker compose down` (con `-v` se borra la base de datos).

## Estado de los módulos

| Módulo | Dueño | Estado |
|---|---|---|
| Usuarios, login, roles y empleados (`usuarios/`, `config/`, `common/`) | A · Alison | Implementado |
| Docker, repo y documentación | E · Alison | Implementado |
| Productos y alertas (`productos/`, `alertas/`) | B · Marco | Implementado |
| Movimientos, ajustes e historial (`movimientos/`) | C · Antonio | Pendiente |
| Frontend: 5 pantallas (`src/main/resources/static/`) | D · Renata | Pendiente |

Las rutas que debe exponer cada módulo están en [`docs/api.md`](docs/api.md).

## Cómo agregar tu módulo

1. Actualiza `main` y crea tu rama: `git pull origin main` y luego `git checkout -b feature/<tu-modulo>`.
2. Trabaja solo en tu paquete dentro de `src/main/java/com/smartinventory/`.
3. Tu tabla va en su propia migración en `src/main/resources/db/migration/`: `V2__productos.sql` (B) o `V3__movimientos.sql` (C).
4. Los permisos por rol ya están definidos en `config/SecurityConfig.java`.
5. Sube tu rama y abre un pull request; otra persona lo revisa antes de unirlo a `main`.

## Estructura del repositorio

```
.
├── pom.xml
├── src/main/java/com/smartinventory/
│   ├── usuarios/       # Login, roles, cuenta inicial y gestión de empleados
│   ├── config/         # Seguridad
│   └── common/         # Manejo de errores
├── src/main/resources/
│   ├── application.properties
│   └── db/migration/   # Migraciones de la base de datos (Flyway)
├── docker-compose.yml
├── docker/backend.Dockerfile
├── .env.example
├── docs/               # Arquitectura, API, pruebas, bitácora, trazabilidad, versionamiento y sprints
└── .github/pull_request_template.md
```

## Flujo de trabajo

- Una rama por tarea: `feature/<módulo>-<descripción>`, `docs/<tema>` o `fix/<descripción>`.
- Nadie hace push directo a `main`: todo entra por pull request revisado por otra persona.
- Detalle en [`docs/versionamiento.md`](docs/versionamiento.md).

## Equipo

| Rol | Integrante | Responsabilidad |
|---|---|---|
| A | Alison | Esqueleto Spring Boot, base de datos, login y roles, gestión de empleados |
| B | Marco | Productos y alertas |
| C | Antonio | Movimientos, ajustes e historial |
| D | Renata | Frontend |
| E | Alison | Docker, README y repo, pruebas, bitácora, arquitectura y stack, versionamiento y trazabilidad |
