package com.smartinventory.usuarios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Crea la cuenta inicial de administrador si todavía no existe ningún usuario. */
@Component
public class AdminInicial implements CommandLineRunner {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final String username;
    private final String password;

    public AdminInicial(UsuarioRepository repo,
                        PasswordEncoder encoder,
                        @Value("${app.admin.username}") String username,
                        @Value("${app.admin.password}") String password) {
        this.repo = repo;
        this.encoder = encoder;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run(String... args) {
        if (repo.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername(username);
            admin.setPasswordHash(encoder.encode(password));
            admin.setNombre("Administrador");
            admin.setRol(Rol.ADMIN);
            admin.setActivo(true);
            repo.save(admin);
        }
    }
}
