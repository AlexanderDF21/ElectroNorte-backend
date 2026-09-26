package com.mesaayuda.service;

import com.mesaayuda.dto.LoginRequest;
import com.mesaayuda.dto.LoginResponse;
import com.mesaayuda.entity.Usuario;
import com.mesaayuda.repository.UsuarioRepository;
import com.mesaayuda.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Credenciales incorrectas"
                        ));

        if (!Boolean.TRUE.equals(usuario.getEstado())) {
            throw new RuntimeException(
                    "El usuario se encuentra desactivado"
            );
        }

        boolean passwordCorrecto =
                passwordEncoder.matches(
                        request.getPassword(),
                        usuario.getPassword()
                );

        if (!passwordCorrecto) {
            throw new RuntimeException(
                    "Credenciales incorrectas"
            );
        }

        String token =
                jwtService.generarToken(
                        usuario.getEmail(),
                        usuario.getRol().name()
                );

        return new LoginResponse(
                usuario.getId(),
                usuario.getNombre()
                        + " "
                        + usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol().name(),
                token
        );
    }
}