package com.smartinventory.movimientos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.smartinventory.common.NegocioException;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientosController {
    private final MovimientoRepository movimientos;
    private final MovimientoService servicio;

    public MovimientosController(MovimientoRepository movimientos, MovimientoService servicio) {
        this.movimientos = movimientos;
        this.servicio = servicio;
    }

    public record Solicitud(@NotNull @Positive Long productoId,
                            @NotNull Movimiento.Tipo tipo,
                            @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal cantidad,
                            @NotBlank @Size(max = 500) String motivo) {}

    public record SolicitudAjuste(@NotNull @Positive Long productoId,
                                  @NotNull @DecimalMin("0") BigDecimal nuevoStock,
                                  @NotBlank @Size(max = 500) String motivo) {}

    @GetMapping
    public List<Movimiento> listar(@RequestParam(required = false) Long productoId,
                                   @RequestParam(required = false) LocalDate desde,
                                   @RequestParam(required = false) LocalDate hasta) {
        if (productoId != null && productoId <= 0) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "productoId debe ser positivo");
        }
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "desde no puede ser posterior a hasta");
        }
        return movimientos.buscar(productoId, desde, hasta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registrar(@Valid @RequestBody Solicitud datos, Principal usuario) {
        servicio.registrar(datos.productoId(), datos.tipo(), datos.cantidad(),
                           datos.motivo(), usuario.getName());
    }

    @PostMapping("/ajustes")
    @ResponseStatus(HttpStatus.CREATED)
    public void ajustar(@Valid @RequestBody SolicitudAjuste datos, Principal usuario) {
        servicio.ajustar(datos.productoId(), datos.nuevoStock(), datos.motivo(), usuario.getName());
    }
}
