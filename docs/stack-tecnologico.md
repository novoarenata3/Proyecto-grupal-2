# Stack tecnológico

Versión 0.2 · Sprint 1 · Responsable: Alison (rol E)

| Capa | Tecnología | Estado | Razón |
|---|---|---|---|
| Lenguaje | Java 17 | Definido | Definido en el alcance del proyecto |
| Backend | Spring Boot 3 | Definido | Definido en el alcance; facilita crear la API REST, la seguridad y el acceso a datos |
| Seguridad | Spring Security con HTTP Basic y contraseñas BCrypt | Definido | Roles Empleado y Administrador sin complejidad extra; sin sesiones en el servidor |
| Gestión de dependencias | Maven | Definido | Opción por defecto de Spring Boot |
| Base de datos | PostgreSQL 16 | Definido | Definido en el alcance; base relacional adecuada para productos y movimientos |
| Migraciones de BD | Flyway | Definido | Cada módulo tiene su archivo de migración y la base se crea sola al arrancar |
| Frontend | HTML, CSS y JavaScript sin framework | Definido | Se sirve desde Spring Boot (carpeta `static`): no necesita servidor aparte ni pasos extra de integración |
| Contenedores | Docker y docker-compose | Definido | Cualquiera levanta el proyecto completo con un solo comando |
| Pruebas | Postman y pruebas manuales documentadas | Definido | Casos en `docs/pruebas-funcionales.md`; JUnit queda como mejora opcional |
| Control de versiones | GitHub | Definido | Ramas, pull requests y revisión cruzada |
| Gestión del proyecto | Notion | Definido | Backlog, kanban y documentación |
| Comunicación | Discord | Definido | Coordinación del equipo y avisos del repositorio |

## Decisiones pendientes

1. Migrar el frontend a un framework (por ejemplo React) solo si sobra tiempo.
2. Agregar pruebas automáticas con JUnit y Spring Boot Test.
