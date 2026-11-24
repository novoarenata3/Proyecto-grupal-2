package com.smartinventory.alertas;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.smartinventory.productos.Producto;
import com.smartinventory.productos.ProductoRepository;

@RestController
@RequestMapping("/api/alertas")
public class AlertasController {
    private final ProductoRepository productos;

    public AlertasController(ProductoRepository productos) { this.productos = productos; }

    @GetMapping
    public List<Producto> listar() { return productos.conAlerta(); }
}
