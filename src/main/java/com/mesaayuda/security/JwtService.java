package com.mesaayuda.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 8;

    private SecretKey obtenerClave() {

        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)        );
    }

    public String generarToken(
            String email,
            String rol) {

        return Jwts.builder()
                .subject(email)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION_TIME
                        )
                )
                .signWith(obtenerClave())
                .compact();
    }

    public Claims obtenerClaims(String token) {

        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String obtenerEmail(String token) {
        return obtenerClaims(token).getSubject();
    }

    public String obtenerRol(String token) {

        return obtenerClaims(token)
                .get("rol", String.class);
    }

    public boolean esTokenValido(String token) {

        try {

            Claims claims = obtenerClaims(token);

            boolean valido = claims.getExpiration()
                    .after(new Date());

            System.out.println("========== JWT ==========");
            System.out.println("Email: " + claims.getSubject());
            System.out.println("Rol: " + claims.get("rol"));
            System.out.println("Expira: " + claims.getExpiration());
            System.out.println("Token válido: " + valido);
            System.out.println("=========================");

            return valido;

        } catch (Exception e) {

            System.out.println("========== ERROR JWT ==========");
            System.out.println("Tipo: " + e.getClass().getName());
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("===============================");

            return false;
        }
    }
}