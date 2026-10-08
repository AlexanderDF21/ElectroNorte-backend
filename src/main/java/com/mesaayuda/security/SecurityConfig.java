package com.mesaayuda.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // ================================
                        // ENDPOINTS PÚBLICOS
                        // ================================

                        .requestMatchers(
                                "/api/auth/**",
                                "/api/prueba",
                                "/error"
                        ).permitAll()

                        // ================================
                        // USUARIOS
                        // SOLO ADMIN
                        // ================================

                        .requestMatchers(
                                "/api/usuarios/**"
                        ).hasRole("ADMIN")

                        // ================================
                        // TIPOS DE INCIDENCIA
                        // ================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/tipos-incidencia/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "TECNICO",
                                "USUARIO"
                        )

                        .requestMatchers(
                                "/api/tipos-incidencia/**"
                        ).hasRole("ADMIN")

                        // ================================
                        // ASIGNAR TÉCNICO
                        // SOLO ADMIN
                        // ================================

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/incidencias/*/asignar"
                        ).hasRole("ADMIN")

                        // ================================
                        // CAMBIAR ESTADO
                        // ADMIN O TECNICO
                        // ================================

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/incidencias/*/estado"
                        ).hasAnyRole(
                                "ADMIN",
                                "TECNICO"
                        )

                        // ================================
                        // REGISTRAR ACCIONES
                        // ADMIN O TECNICO
                        // ================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/incidencias/*/acciones"
                        ).hasAnyRole(
                                "ADMIN",
                                "TECNICO"
                        )

                        // ================================
                        // CREAR INCIDENCIA
                        // ADMIN O USUARIO
                        // ================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/incidencias"
                        ).hasAnyRole(
                                "ADMIN",
                                "USUARIO"
                        )

                        // ================================
                        // CONSULTAR INCIDENCIAS
                        // ================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/incidencias/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "TECNICO",
                                "USUARIO"
                        )
                        .requestMatchers("/api/reportes/**")
                        .hasRole("ADMIN")

                        // ================================
                        // RESTO DE ENDPOINTS
                        // ================================

                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception

                        // ==================================
                        // NO AUTENTICADO -> 401
                        // ==================================

                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.setCharacterEncoding(
                                            "UTF-8"
                                    );

                                    response.getWriter().write(
                                            """
                                            {
                                                "status": 401,
                                                "mensaje": "No autorizado"
                                            }
                                            """
                                    );
                                }
                        )

                        // ==================================
                        // AUTENTICADO SIN PERMISO -> 403
                        // ==================================

                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.setCharacterEncoding(
                                            "UTF-8"
                                    );

                                    response.getWriter().write(
                                            """
                                            {
                                                "status": 403,
                                                "mensaje": "Acceso denegado"
                                            }
                                            """
                                    );
                                }
                        )
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:4200",
                        "https://lemon-cliff-0e380020f.6.azurestaticapps.net"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}