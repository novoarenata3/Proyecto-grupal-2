# Política de versionamiento

Versión 0.1 · Responsable: Alison (rol E)

## Ramas

| Rama | Uso |
|---|---|
| `main` | Versión estable. Nadie hace push directo; solo entra lo aprobado por pull request |
| `feature/<módulo>-<descripción>` | Funcionalidades. Ejemplo: `feature/alertas-calculo-stock` |
| `docs/<tema>` | Documentación. Ejemplo: `docs/qa-pruebas` |
| `fix/<descripción>` | Corrección de errores |

## Commits

Formato: `tipo: mensaje corto en presente`.

| Tipo | Cuándo |
|---|---|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de un error |
| `docs` | Documentación |
| `test` | Casos o pruebas |
| `chore` | Configuración, Docker u otras tareas |

Ejemplo: `feat: calcula la compra sugerida en alertas`.

## Pull requests

1. Se abre con la plantilla del repositorio.
2. Lo revisa al menos otra persona antes de unirse a `main`.
3. Se enlaza con la tarjeta del kanban y la historia de usuario.

## Versiones del software

Se usa versionamiento semántico (`MAYOR.MENOR.PARCHE`). Al cerrar cada sprint se crea una etiqueta (tag) en GitHub.

| Versión | Cuándo |
|---|---|
| `v0.1.0` | Fin del Sprint 1: repositorio, documentación base y Docker |
| `v0.2.0` | Fin del Sprint 2: backend con módulos principales |
| `v1.0.0` | Entrega final |

## Versiones de documentos y diagramas

- Los documentos de `docs/` se versionan con Git: cada cambio es un commit y se puede ver el historial.
- Cada documento tiene un número de versión en el encabezado, que sube cuando cambia su contenido de fondo.
- Los diagramas están escritos en Mermaid dentro de los `.md`, por lo que GitHub los dibuja y su historial queda registrado.

## Decisiones de arquitectura (ADR)

- Se guardan en `docs/decisiones/` con nombre `ADR-NNN-titulo.md`.
- Estados: Propuesta, Aceptada, Reemplazada.
- Nunca se borran. Si una decisión cambia, se crea una ADR nueva y la anterior pasa a Reemplazada.
