package com.smartinventory.movimientos;

import com.smartinventory.common.NegocioException;
import com.smartinventory.productos.Producto;
import com.smartinventory.productos.ProductoRepository;
import com.smartinventory.usuarios.UsuarioRepository;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimientoService {
    private final ProductoRepository productos;
    private final UsuarioRepository usuarios;
    private final MovimientoRepository movimientos;

    public MovimientoService(ProductoRepository productos, UsuarioRepository usuarios,
                              MovimientoRepository movimientos) {
        this.productos = productos;
        this.usuarios = usuarios;
        this.movimientos = movimientos;
    }

    @Transactional
    public void registrar(long productoId, Movimiento.Tipo tipo, BigDecimal cantidad,
                          String motivo, String username) {
        if (tipo != Movimiento.Tipo.ENTRADA && tipo != Movimiento.Tipo.SALIDA) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "El tipo debe ser ENTRADA o SALIDA");
        }
        if (cantidad == null || cantidad.signum() <= 0) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a cero");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "El motivo es obligatorio");
        }

        Long usuarioId = usuarios.findByUsername(username)
                .orElseThrow(() -> new NegocioException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"))
                .getId();

        Producto producto = productos.bloquear(productoId)
                .orElseThrow(() -> new NegocioException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        if (!producto.isActivo()) {
            throw new NegocioException(HttpStatus.CONFLICT, "El producto está desactivado");
        }

        String unidad = producto.getUnidad();
        if (("unidad".equalsIgnoreCase(unidad) || "unidades".equalsIgnoreCase(unidad))
                && cantidad.stripTrailingZeros().scale() > 0) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "Para unidades, la cantidad debe ser entera");
        }

        BigDecimal stockAnterior = producto.getStock();
        BigDecimal stockResultante = tipo == Movimiento.Tipo.ENTRADA
                ? stockAnterior.add(cantidad)
                : stockAnterior.subtract(cantidad);
        if (stockResultante.signum() < 0) {
            throw new NegocioException(HttpStatus.CONFLICT, "La salida supera el stock disponible");
        }

        producto.cambiarStock(stockResultante);
        movimientos.save(new Movimiento(productoId, usuarioId, tipo, motivo.strip(),
                                         stockAnterior, stockResultante));
    }

    @Transactional
    public void ajustar(long productoId, BigDecimal nuevoStock, String motivo, String username) {
        if (nuevoStock == null || nuevoStock.signum() < 0) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "El nuevo stock debe ser mayor o igual a cero");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "El motivo es obligatorio");
        }

        Long usuarioId = usuarios.findByUsername(username)
                .orElseThrow(() -> new NegocioException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"))
                .getId();
        Producto producto = productos.bloquear(productoId)
                .orElseThrow(() -> new NegocioException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        if (!producto.isActivo()) {
            throw new NegocioException(HttpStatus.CONFLICT, "El producto está desactivado");
        }
        validarCantidadPorUnidad(producto, nuevoStock);

        BigDecimal stockAnterior = producto.getStock();
        if (nuevoStock.compareTo(stockAnterior) == 0) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "El nuevo stock debe ser distinto al actual");
        }

        producto.cambiarStock(nuevoStock);
        movimientos.save(new Movimiento(productoId, usuarioId, Movimiento.Tipo.AJUSTE, motivo.strip(),
                                         stockAnterior, nuevoStock));
    }

    private void validarCantidadPorUnidad(Producto producto, BigDecimal cantidad) {
        String unidad = producto.getUnidad();
        if (("unidad".equalsIgnoreCase(unidad) || "unidades".equalsIgnoreCase(unidad))
                && cantidad.stripTrailingZeros().scale() > 0) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "Para unidades, la cantidad debe ser entera");
        }
    }
}
