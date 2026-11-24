package com.smartinventory.usuarios;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearEmpleado(
        @NotBlank(message = "El usuario es obligatorio")
        @Size(max = 50, message = "El usuario admite hasta 50 caracteres")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
        String password,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre admite hasta 100 caracteres")
        String nombre) {
}
