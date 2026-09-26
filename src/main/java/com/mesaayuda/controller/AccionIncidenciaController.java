package com.mesaayuda.controller;

import com.mesaayuda.entity.AccionIncidencia;
import com.mesaayuda.service.AccionIncidenciaService;
import com.mesaayuda.service.IncidenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incidencias")
@CrossOrigin(origins = "http://localhost:4200")
public class AccionIncidenciaController {

    private final AccionIncidenciaService accionService;
    private final IncidenciaService incidenciaService;

    public AccionIncidenciaController(
            AccionIncidenciaService accionService,
            IncidenciaService incidenciaService) {

        this.accionService = accionService;
        this.incidenciaService = incidenciaService;
    }

    @PostMapping("/{incidenciaId}/acciones")
    public ResponseEntity<AccionIncidencia> registrarAccion(
            @PathVariable Long incidenciaId,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        String email = authentication.getName();

        String rol = authentication
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        if (!"TECNICO".equals(rol)) {
            return ResponseEntity.status(403).build();
        }

        String descripcion = body.get("descripcion");

        AccionIncidencia accion =
                accionService.registrarAccion(
                        incidenciaId,
                        email,
                        descripcion
                );

        return ResponseEntity.ok(accion);
    }

    @GetMapping("/{incidenciaId}/acciones")
    public ResponseEntity<List<AccionIncidencia>> listarAcciones(
            @PathVariable Long incidenciaId,
            Authentication authentication) {

        String email = authentication.getName();

        String rol = authentication
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        boolean permitido =
                incidenciaService.puedeAccederIncidencia(
                        incidenciaId,
                        email,
                        rol
                );

        if (!permitido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                accionService.listarPorIncidencia(incidenciaId)
        );
    }
}