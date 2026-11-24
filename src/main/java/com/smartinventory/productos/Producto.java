package com.smartinventory.productos;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 30, updatable = false)
    private String unidad;

    @Column(name = "stock_minimo", nullable = false)
    private BigDecimal stockMinimo;

    @Column(name = "cantidad_objetivo", nullable = false)
    private BigDecimal cantidadObjetivo;

    @Column(nullable = false, insertable = false, updatable = false)
    private BigDecimal stock = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean activo = true;

    protected Producto() {}

    public Producto(String nombre, String unidad, BigDecimal minimo, BigDecimal objetivo) {
        this.unidad = texto(unidad, 30, "unidad");
        editar(nombre, minimo, objetivo);
    }

    public void editar(String nombre, BigDecimal minimo, BigDecimal objetivo) {
        String nuevoNombre = texto(nombre, 120, "nombre");
        if (minimo == null || minimo.signum() < 0 || objetivo == null || objetivo.compareTo(minimo) <= 0) {
            throw new IllegalArgumentException("El mínimo debe ser no negativo y el objetivo debe superarlo");
        }
        if (("unidad".equalsIgnoreCase(unidad) || "unidades".equalsIgnoreCase(unidad))
                && (minimo.stripTrailingZeros().scale() > 0 || objetivo.stripTrailingZeros().scale() > 0)) {
            throw new IllegalArgumentException("Para unidades, las cantidades deben ser enteras");
        }
        this.nombre = nuevoNombre;
        this.stockMinimo = minimo;
        this.cantidadObjetivo = objetivo;
    }

    private static String texto(String valor, int limite, String campo) {
        if (valor == null || valor.isBlank() || valor.strip().length() > limite) {
            throw new IllegalArgumentException("Valor inválido para " + campo);
        }
        return valor.strip();
    }

    public void desactivar() { activo = false; }
    public void reactivar() { activo = true; }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUnidad() { return unidad; }
    public BigDecimal getStockMinimo() { return stockMinimo; }
    public BigDecimal getCantidadObjetivo() { return cantidadObjetivo; }
    public BigDecimal getStock() { return stock; }
    public boolean isActivo() { return activo; }
    public boolean isEnAlerta() { return activo && stock.compareTo(stockMinimo) <= 0; }
    public BigDecimal getCompraSugerida() {
        return isEnAlerta() ? cantidadObjetivo.subtract(stock) : BigDecimal.ZERO;
    }
}
