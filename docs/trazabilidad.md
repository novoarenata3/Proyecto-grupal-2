# Trazabilidad

Versión 0.1 · Responsable: Alison (rol E)

Conecta cada requisito del alcance con su historia de usuario, el módulo que lo implementa, su dueño y las pruebas que lo verifican. Las historias son provisionales hasta que el backlog final esté en Notion. La columna Dueño es quien implementa cada módulo (ver [sprints/sprint-01.md](sprints/sprint-01.md)).

| Requisito del alcance | Historia | Módulo | Dueño | Pruebas |
|---|---|---|---|---|
| Inicio de sesión y roles | HU-USR-01 | Usuarios | A · Alison | CP-SEG-01, CP-SEG-02, CP-SEG-03 |
| Gestión de cuentas de empleados | HU-USR-02 | Usuarios | A · Alison | CP-SEG-04 |
| Crear producto con validaciones | HU-PRO-01 | Productos | B · Marco | CP-PRO-01, CP-PRO-02, CP-PRO-03, CP-PRO-04 |
| Eliminar, desactivar y reactivar productos | HU-PRO-02 | Productos | B · Marco | CP-PRO-05, CP-PRO-06, CP-PRO-07, CP-PRO-08 |
| Alerta cuando el stock es igual o inferior al mínimo | HU-ALR-01 | Alertas | B · Marco | CP-ALR-01, CP-ALR-02, CP-ALR-03, CP-ALR-08, CP-ALR-10, CP-ALR-11 |
| La alerta se resuelve al superar el mínimo | HU-ALR-02 | Alertas | B · Marco | CP-ALR-04, CP-ALR-05, CP-ALR-09 |
| Sugerencia de compra (objetivo − stock) | HU-ALR-03 | Alertas | B · Marco | CP-ALR-06, CP-ALR-07 |
| Entradas y salidas con fecha y responsable automáticos | HU-MOV-01 | Movimientos | C · Antonio | CP-MOV-01, CP-MOV-03 |
| Bloquear salidas mayores al stock | HU-MOV-02 | Movimientos | C · Antonio | CP-MOV-02 |
| Inventario inicial | HU-MOV-03 | Movimientos | C · Antonio | CP-MOV-04 |
| Ajustes solo del administrador con motivo | HU-MOV-04 | Movimientos | C · Antonio | CP-MOV-05, CP-MOV-06, CP-MOV-07 |
| Historial filtrado por producto y fecha | HU-MOV-05 | Movimientos | C · Antonio | CP-MOV-08 |
| Cinco pantallas de la aplicación | HU-FRT-01 | Frontend | D · Renata | Pruebas manuales de interfaz |
| Replicabilidad con Docker | HU-DOC-01 | Docker | E · Alison | CP-DOC-01 |
