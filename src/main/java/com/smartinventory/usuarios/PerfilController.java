package com.smartinventory.usuarios;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartinventory.common.NegocioException;

@RestController
public class PerfilController {

    private final UsuarioRepository repo;

    public PerfilController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/api/me")
    public Map<String, String> perfil(Authentication auth) {
        Usuario u = repo.findByUsername(auth.getName())
                .orElseThrow(() -> new NegocioException(HttpStatus.UNAUTHORIZED, "Sesión no válida"));
        return Map.of("username", u.getUsername(), "nombre", u.getNombre(), "rol", u.getRol().name());
    }
}
