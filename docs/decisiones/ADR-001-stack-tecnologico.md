# ADR-001: Stack tecnológico

- **Estado:** Aceptada
- **Fecha:** 19/11/2026
- **Responsable:** Alison (rol E)

## Contexto

El alcance del proyecto define Java, Spring Boot y PostgreSQL. El frontend aún no está definido y el equipo tiene dos semanas.

## Decisión

Backend en Java con Spring Boot, base de datos PostgreSQL y Docker para levantar el entorno. El frontend se decidirá en el Sprint 1.

## Consecuencias

- Todo el equipo trabaja contra la misma base de datos usando `docker compose up`.
- El backend queda dividido en módulos por paquete (usuarios, productos, movimientos, alertas).
- La decisión del frontend queda pendiente y puede afectar el tiempo de integración.
