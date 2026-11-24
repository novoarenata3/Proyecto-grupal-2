package com.smartinventory.productos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductosController {
    private final ProductoRepository productos;

    public ProductosController(ProductoRepository productos) { this.productos = productos; }

    public record Alta(String nombre, String unidad, BigDecimal stockMinimo, BigDecimal cantidadObjetivo) {}
    public record Edicion(String nombre, BigDecimal stockMinimo, BigDecimal cantidadObjetivo) {}

    @GetMapping
    public List<Producto> listar() { return productos.findAll(Sort.by("nombre", "id")); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Alta datos) {
        return productos.save(new Producto(datos.nombre(), datos.unidad(), datos.stockMinimo(), datos.cantidadObjetivo()));
    }

    @PutMapping("/{id}")
    @Transactional
    public Producto editar(@PathVariable("id") long id, @RequestBody Edicion datos) {
        Producto producto = buscar(id);
        producto.editar(datos.nombre(), datos.stockMinimo(), datos.cantidadObjetivo());
        return producto;
    }

    @PatchMapping("/{id}/desactivar")
    @Transactional
    public Producto desactivar(@PathVariable("id") long id) {
        Producto producto = buscar(id);
        producto.desactivar();
        return producto;
    }

    @PatchMapping("/{id}/reactivar")
    @Transactional
    public Producto reactivar(@PathVariable("id") long id) {
        Producto producto = buscar(id);
        producto.reactivar();
        return producto;
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") long id) {
        Producto producto = buscar(id);
        if (producto.getStock().signum() != 0) throw new ProductoEnUso();
        productos.delete(producto);
        try {
            productos.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ProductoEnUso();
        }
    }

    private Producto buscar(long id) {
        return productos.bloquear(id).orElseThrow(ProductoAusente::new);
    }

    private static class ProductoAusente extends RuntimeException {}
    private static class ProductoEnUso extends RuntimeException {}

    @ExceptionHandler(ProductoAusente.class)
    public ResponseEntity<Map<String,String>> ausente() {
        return ResponseEntity.status(404).body(Map.of("error", "Producto no encontrado"));
    }

    @ExceptionHandler(ProductoEnUso.class)
    public ResponseEntity<Map<String,String>> enUso() {
        return ResponseEntity.status(409).body(Map.of("error", "El producto tiene stock o registros asociados"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> invalido(IllegalArgumentException error) {
        return ResponseEntity.badRequest().body(Map.of("error", error.getMessage()));
    }
}
