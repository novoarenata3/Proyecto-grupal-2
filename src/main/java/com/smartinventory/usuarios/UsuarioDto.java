package com.smartinventory.usuarios;

public record UsuarioDto(Long id, String username, String nombre, Rol rol, boolean activo) {

    public static UsuarioDto de(Usuario u) {
        return new UsuarioDto(u.getId(), u.getUsername(), u.getNombre(), u.getRol(), u.isActivo());
    }
}
