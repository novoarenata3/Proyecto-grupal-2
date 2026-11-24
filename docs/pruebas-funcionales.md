# Pruebas funcionales

Versión 0.1 · Responsable: Alison (rol E)

Cada caso sale de una regla del alcance. Las columnas "Resultado real" y "Estado" se llenan al ejecutar la prueba (API con Postman y pantallas a mano). Estados: Pendiente, Pasó, Falló.

## Cómo preparar el entorno de pruebas

1. Levanta el proyecto con `docker compose up -d --build` y abre <http://localhost:8080>.
2. Entra como administrador (`admin` / `Admin123!`) y crea dos cuentas de empleado para probar los permisos.
3. Crea los productos de ejemplo de cafetería (Leche en litros, Café en grano en kilogramos, Vasos en unidades) y carga su stock con una entrada de motivo `Inventario inicial`.
4. Para probar la API en Postman usa autenticación Basic y las rutas de [api.md](api.md).

## Alertas (ejemplo de cafetería)

Datos base: Leche, litros, mínimo 10, objetivo 30. Café en grano, kilogramos, mínimo 2,5, objetivo 10.

| ID | Requisito | Pasos | Resultado esperado | Resultado real | Estado |
|---|---|---|---|---|---|
| CP-ALR-01 | Alerta bajo el mínimo | Leche con stock 25 L; registrar salida de 17 L | Stock 8 L; aparece alerta de Leche | | Pendiente |
| CP-ALR-02 | Alerta igual al mínimo | Café con stock 4 kg; registrar salida de 1,5 kg | Stock 2,5 kg; aparece alerta de Café | | Pendiente |
| CP-ALR-03 | Sin alerta sobre el mínimo | Leche con stock 28 L; registrar salida de 17 L | Stock 11 L; no hay alerta | | Pendiente |
| CP-ALR-04 | Resolución de la alerta | Leche con alerta (8 L); registrar entrada de 5 L | Stock 13 L; la alerta desaparece | | Pendiente |
| CP-ALR-05 | Entrada que no supera el mínimo | Leche con alerta (8 L); registrar entrada de 2 L | Stock 10 L; la alerta sigue activa | | Pendiente |
| CP-ALR-06 | Compra sugerida | Leche con stock 8 L | La alerta sugiere comprar 22 L (30 − 8) | | Pendiente |
| CP-ALR-07 | Compra sugerida con decimales | Café con stock 2,5 kg | La alerta sugiere comprar 7,5 kg | | Pendiente |
| CP-ALR-08 | Visible para ambos roles | Iniciar sesión como Empleado y como Administrador | Ambos ven la misma alerta | | Pendiente |
| CP-ALR-09 | Ajuste recalcula la alerta | Administrador ajusta el stock de Leche de 8 L a 20 L con motivo | La alerta desaparece | | Pendiente |
| CP-ALR-10 | Producto nuevo en cero | Crear un producto con mínimo mayor que 0 | Comienza en stock 0 y aparece en alerta hasta el inventario inicial (por confirmar) | | Pendiente |
| CP-ALR-11 | Producto desactivado | Desactivar un producto que estaba en alerta | No genera alertas (por confirmar) | | Pendiente |

## Productos

| ID | Requisito | Pasos | Resultado esperado | Resultado real | Estado |
|---|---|---|---|---|---|
| CP-PRO-01 | Objetivo mayor que el mínimo | Crear producto con mínimo 10 y objetivo 10 | El sistema lo rechaza con mensaje claro | | Pendiente |
| CP-PRO-02 | Unidades solo enteros | Crear producto en unidades y registrar entrada de 2,5 | Se rechaza | | Pendiente |
| CP-PRO-03 | Kg y litros con decimales | Registrar entrada de 2,5 kg de café | Se acepta | | Pendiente |
| CP-PRO-04 | Inicio en cero | Crear un producto nuevo | Su stock inicial es 0 | | Pendiente |
| CP-PRO-05 | Eliminar sin movimientos | Administrador elimina un producto sin movimientos | Se elimina | | Pendiente |
| CP-PRO-06 | Producto con movimientos | Administrador intenta eliminar un producto con movimientos | No se elimina; se ofrece desactivarlo | | Pendiente |
| CP-PRO-07 | Desactivado bloquea movimientos | Registrar un movimiento sobre un producto desactivado | Se rechaza y el historial se conserva | | Pendiente |
| CP-PRO-08 | Reactivar | Administrador reactiva un producto desactivado | Vuelve a admitir movimientos | | Pendiente |

## Movimientos e historial

| ID | Requisito | Pasos | Resultado esperado | Resultado real | Estado |
|---|---|---|---|---|---|
| CP-MOV-01 | Datos automáticos | Registrar una entrada con producto, cantidad y motivo | Quedan guardados fecha y responsable sin que el usuario los escriba | | Pendiente |
| CP-MOV-02 | Salida mayor al stock | Registrar salida de 30 L con stock 25 L | Se bloquea con mensaje claro | | Pendiente |
| CP-MOV-03 | Salida igual al stock | Registrar salida de 25 L con stock 25 L | Se permite; stock 0 | | Pendiente |
| CP-MOV-04 | Inventario inicial | Administrador registra una entrada con motivo «Inventario inicial» | El stock sube y el movimiento queda en el historial | | Pendiente |
| CP-MOV-05 | Ajuste con motivo obligatorio | Administrador intenta ajustar sin motivo | Se rechaza | | Pendiente |
| CP-MOV-06 | Ajuste conserva registros | Administrador ajusta el stock con motivo | El ajuste se registra y los movimientos originales siguen intactos | | Pendiente |
| CP-MOV-07 | Empleado sin ajustes | Empleado intenta realizar un ajuste | Se rechaza por falta de permisos | | Pendiente |
| CP-MOV-08 | Filtros del historial | Filtrar por producto y por rango de fechas | Solo aparecen los movimientos que cumplen | | Pendiente |

## Seguridad y usuarios

| ID | Requisito | Pasos | Resultado esperado | Resultado real | Estado |
|---|---|---|---|---|---|
| CP-SEG-01 | Inicio de sesión | Entrar con credenciales válidas y con inválidas | Las válidas ingresan; las inválidas se rechazan | | Pendiente |
| CP-SEG-02 | Cuenta inicial | Entrar con la cuenta de administrador entregada | Ingresa con rol Administrador | | Pendiente |
| CP-SEG-03 | Empleado sin gestión | Empleado intenta crear productos o cuentas | Se rechaza | | Pendiente |
| CP-SEG-04 | Crear empleados | Administrador crea una cuenta de empleado | La cuenta puede iniciar sesión con rol Empleado | | Pendiente |

## Entorno

| ID | Requisito | Pasos | Resultado esperado | Resultado real | Estado |
|---|---|---|---|---|---|
| CP-DOC-01 | Replicabilidad | En una carpeta limpia: copiar `.env.example` a `.env` y ejecutar `docker compose up -d` | El servicio `db` queda en estado `healthy` | | Pendiente |

## Cómo registrar una prueba

1. Ejecuta los pasos y anota el resultado obtenido.
2. Cambia el estado a Pasó o Falló y guarda una captura de pantalla (carpeta `docs/evidencias/`, opcional).
3. Si falla, abre un issue en GitHub con los pasos, el resultado esperado y el obtenido, asígnalo al responsable y anótalo en la [bitácora](bitacora-cambios.md).
