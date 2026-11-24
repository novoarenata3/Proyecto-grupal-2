package com.smartinventory.usuarios;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartinventory.common.NegocioException;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> listar() {
        return repo.findAll(Sort.by("username")).stream().map(UsuarioDto::de).toList();
    }

    @Transactional
    public UsuarioDto crear(CrearEmpleado req) {
        String username = req.username().trim();
        if (repo.existsByUsername(username)) {
            throw new NegocioException(HttpStatus.CONFLICT, "Ya existe un usuario con ese nombre");
        }
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPasswordHash(encoder.encode(req.password()));
        u.setNombre(req.nombre().trim());
        u.setRol(Rol.EMPLEADO);
        u.setActivo(true);
        return UsuarioDto.de(repo.save(u));
    }

    @Transactional
    public UsuarioDto cambiarEstado(Long id, boolean activo) {
        Usuario u = buscar(id);
        if (!activo && u.getRol() == Rol.ADMIN) {
            throw new NegocioException(HttpStatus.CONFLICT, "No se puede desactivar una cuenta de administrador");
        }
        u.setActivo(activo);
        return UsuarioDto.de(u);
    }

    @Transactional
    public void cambiarPassword(Long id, String password) {
        Usuario u = buscar(id);
        if (u.getRol() == Rol.ADMIN) {
            throw new NegocioException(HttpStatus.CONFLICT, "Solo se gestionan las cuentas de empleados");
        }
        u.setPasswordHash(encoder.encode(password));
    }

    private Usuario buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NegocioException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }
}
