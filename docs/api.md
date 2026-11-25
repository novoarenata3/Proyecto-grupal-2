# Endpoints de la API

Versión 0.1. Todas las rutas (salvo la interfaz web) requieren autenticación HTTP Basic. Los errores devuelven `{"error": "mensaje"}`.

> Estado: `/api/me` y `/api/empleados` ya están implementados (rol A). El resto de rutas es el **contrato acordado** para que cada dueño implemente su módulo con los mismos nombres y datos: productos y alertas (B · Marco) y movimientos (C · Antonio).

| Método | Ruta | Rol | Descripción |
|---|---|---|---|
| GET | `/api/me` | Cualquiera | Datos del usuario autenticado |
| GET | `/api/empleados` | Administrador | Lista de cuentas |
| POST | `/api/empleados` | Administrador | Crea un empleado (`username`, `password`, `nombre`) |
| PATCH | `/api/empleados/{id}/desactivar` | Administrador | Desactiva la cuenta de un empleado |
| PATCH | `/api/empleados/{id}/reactivar` | Administrador | Reactiva la cuenta |
| PUT | `/api/empleados/{id}/password` | Administrador | Cambia la contraseña (`password`) |
| GET | `/api/productos` | Cualquiera | Lista de productos con su stock y estado de alerta |
| POST | `/api/productos` | Administrador | Crea un producto (`nombre`, `unidad`, `stockMinimo`, `cantidadObjetivo`); inicia en stock 0 |
| PUT | `/api/productos/{id}` | Administrador | Edita nombre, mínimo y objetivo |
| PATCH | `/api/productos/{id}/desactivar` | Administrador | Desactiva el producto (conserva su historial) |
| PATCH | `/api/productos/{id}/reactivar` | Administrador | Reactiva el producto |
| DELETE | `/api/productos/{id}` | Administrador | Elimina el producto solo si no tiene movimientos |
| GET | `/api/alertas` | Cualquiera | Productos con stock igual o inferior al mínimo y su compra sugerida |
| POST | `/api/movimientos` | Cualquiera | Registra una entrada o salida (`productoId`, `tipo`, `cantidad`, `motivo`) |
| POST | `/api/movimientos/ajustes` | Administrador | Ajusta el stock (`productoId`, `nuevoStock`, `motivo`) |
| GET | `/api/movimientos` | Cualquiera | Historial; filtros opcionales `productoId`, `desde` y `hasta` (`yyyy-MM-dd`) |

Para `POST /api/movimientos`, `tipo` admite `ENTRADA` o `SALIDA`; `cantidad` debe ser positiva y `motivo` obligatorio. El ajuste recibe `productoId`, `nuevoStock` (cero o mayor) y `motivo`; se registra como un movimiento `AJUSTE` con el stock anterior y el resultante. En el historial, `desde` y `hasta` son fechas inclusivas en UTC y se pueden combinar con `productoId`.

## Códigos de respuesta habituales

| Código | Cuándo |
|---|---|
| 200 / 201 / 204 | Operación correcta |
| 400 | Datos inválidos (objetivo menor o igual al mínimo, decimales en unidades, motivo vacío) |
| 401 | Credenciales inválidas o ausentes |
| 403 | El rol no tiene permiso (por ejemplo, un empleado intentando ajustar) |
| 404 | Producto o usuario no encontrado |
| 409 | Conflicto de negocio (salida mayor al stock, producto desactivado, eliminar con movimientos) |
