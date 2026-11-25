package com.smartinventory.movimientos;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "movimientos")
public class Movimiento {
    public enum Tipo { ENTRADA, SALIDA, AJUSTE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    private String motivo;

    @Column(name = "stock_anterior")
    private BigDecimal stockAnterior;

    @Column(name = "stock_resultante")
    private BigDecimal stockResultante;

    private Instant fecha;

    protected Movimiento() {
    }
    
    Movimiento(Long productoId, Long usuarioId, Tipo tipo, String motivo,
            BigDecimal stockAnterior, BigDecimal stockResultante) {
        this.productoId = productoId;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.motivo = motivo;
        this.stockAnterior = stockAnterior;
        this.stockResultante = stockResultante;
        this.fecha = Instant.now();
    }

    public Long getId() { return id; }
    public Long getProductoId() { return productoId; }
    public Long getUsuarioId() { return usuarioId; }
    public Tipo getTipo() { return tipo; }
    public String getMotivo() { return motivo; }
    public BigDecimal getStockAnterior() { return stockAnterior; }
    public BigDecimal getStockResultante() { return stockResultante; }
    public Instant getFecha() { return fecha; }
}
