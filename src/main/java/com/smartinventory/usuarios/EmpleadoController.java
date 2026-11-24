package com.smartinventory.usuarios;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final UsuarioService servicio;

    public EmpleadoController(UsuarioService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<UsuarioDto> listar() {
        return servicio.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto crear(@Valid @RequestBody CrearEmpleado req) {
        return servicio.crear(req);
    }

    @PatchMapping("/{id}/desactivar")
    public UsuarioDto desactivar(@PathVariable Long id) {
        return servicio.cambiarEstado(id, false);
    }

    @PatchMapping("/{id}/reactivar")
    public UsuarioDto reactivar(@PathVariable Long id) {
        return servicio.cambiarEstado(id, true);
    }

    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cambiarPassword(@PathVariable Long id, @Valid @RequestBody CambiarPassword req) {
        servicio.cambiarPassword(id, req.password());
    }
}
