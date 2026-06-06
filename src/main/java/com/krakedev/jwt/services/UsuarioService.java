package com.krakedev.jwt.services;

import com.krakedev.jwt.entidades.Usuario;
import com.krakedev.jwt.repositories.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrarTextoPlano(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario registrarConBCrypt(Usuario usuario) {
        // Generamos un salt y hasheamos la contraseña de forma unidireccional
        String hashedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
        usuario.setPassword(hashedPassword);
        return usuarioRepository.save(usuario);
    }

    // Proceso de Login / Validación
    public Optional<Usuario> login(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Validación 
            if (usuario.getPassword().equals(password)) {
                return Optional.of(usuario);
            }
            
            try {
                if (BCrypt.checkpw(password, usuario.getPassword())) {
                    return Optional.of(usuario);
                }
            } catch (IllegalArgumentException e) {
            }
        }
        return Optional.empty();
    }
}