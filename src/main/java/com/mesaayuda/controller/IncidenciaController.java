package com.mesaayuda.controller;

import com.mesaayuda.dto.IncidenciaRequest;
import com.mesaayuda.dto.IncidenciaResponse;
import com.mesaayuda.entity.HistorialIncidencia;
import com.mesaayuda.entity.Incidencia;
import com.mesaayuda.enums.EstadoIncidencia;
import com.mesaayuda.service.HistorialIncidenciaService;
import com.mesaayuda.service.IncidenciaService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidencias")
@CrossOrigin(origins = "http://localhost:4200")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;

    private final HistorialIncidenciaService
            historialIncidenciaService;

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public IncidenciaController(
            IncidenciaService incidenciaService,
            HistorialIncidenciaService historialIncidenciaService) {

        this.incidenciaService =
                incidenciaService;

        this.historialIncidenciaService =
                historialIncidenciaService;
    }

    // =========================================
    // LISTAR INCIDENCIAS
    // =========================================
    //
    // Devuelve las incidencias según
    // el usuario autenticado.
    //
    // IMPORTANTE:
    // Ahora devuelve IncidenciaResponse
    // en lugar de Incidencia.
    //
    // Esto permite que Angular reciba:
    //
    // usuarioNombre
    // tecnicoNombre
    // tipoIncidenciaNombre
    //
    // =========================================

    @GetMapping
    public List<IncidenciaResponse> listarIncidencias(
            Authentication authentication) {

        String email =
                authentication.getName();

        String rol =
                authentication
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
                        .replace("ROLE_", "");

        List<Incidencia> incidencias =
                incidenciaService
                        .listarSegunUsuario(
                                email,
                                rol
                        );

        return incidencias
                .stream()
                .map(
                        incidenciaService
                                ::convertirAResponse
                )
                .toList();
    }

    // =========================================
    // BUSCAR INCIDENCIA POR ID
    // =========================================
    //
    // Devuelve el detalle completo
    // usando IncidenciaResponse.
    //
    // También verifica que el usuario
    // autenticado tenga acceso.
    //
    // =========================================

    @GetMapping("/{id}")
    public ResponseEntity<IncidenciaResponse> buscarPorId(
            @PathVariable Long id,
            Authentication authentication) {

        String email =
                authentication.getName();

        String rol =
                authentication
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
                        .replace("ROLE_", "");

        return incidenciaService
                .buscarPorIdSegunUsuario(
                        id,
                        email,
                        rol
                )
                .map(
                        incidenciaService
                                ::convertirAResponse
                )
                .map(
                        ResponseEntity::ok
                )
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }

    // =========================================
    // CREAR INCIDENCIA
    // =========================================
    //
    // USUARIO:
    // crea la incidencia para sí mismo
    // utilizando su JWT.
    //
    // ADMIN:
    // puede crear una incidencia indicando
    // usuarioId en IncidenciaRequest.
    //
    // =========================================

    @PostMapping
    public ResponseEntity<IncidenciaResponse> crear(
            @Valid
            @RequestBody IncidenciaRequest request,
            Authentication authentication) {

        String email =
                authentication.getName();

        String rol =
                authentication
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
                        .replace("ROLE_", "");

        IncidenciaResponse nueva =
                incidenciaService.crear(
                        request,
                        email,
                        rol
                );

        return ResponseEntity.ok(
                nueva
        );
    }

    // =========================================
    // ASIGNAR TÉCNICO
    // =========================================
    //
    // Utilizado por ADMIN.
    //
    // Flujo:
    //
    // EN_REVISION
    //      ↓
    // ASIGNADA
    //
    // =========================================

    @PutMapping("/{id}/asignar")
    public ResponseEntity<Incidencia> asignarTecnico(
            @PathVariable Long id,
            @RequestParam Long tecnicoId) {

        Incidencia incidencia =
                incidenciaService
                        .asignarTecnico(
                                id,
                                tecnicoId
                        );

        return ResponseEntity.ok(
                incidencia
        );
    }

    // =========================================
    // CAMBIAR ESTADO
    // =========================================
    //
    // El servicio valida qué transiciones
    // puede realizar cada rol.
    //
    // ADMIN:
    //
    // REGISTRADA -> EN_REVISION
    // RESUELTA   -> CERRADA
    //
    // TECNICO:
    //
    // ASIGNADA    -> EN_ATENCION
    // EN_ATENCION -> RESUELTA
    //
    // =========================================

    @PutMapping("/{id}/estado")
    public ResponseEntity<Incidencia> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoIncidencia estado,
            Authentication authentication) {

        String email =
                authentication.getName();

        String rol =
                authentication
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
                        .replace("ROLE_", "");

        Incidencia incidencia =
                incidenciaService
                        .cambiarEstadoSegunUsuario(
                                id,
                                estado,
                                email,
                                rol
                        );

        return ResponseEntity.ok(
                incidencia
        );
    }

    // =========================================
    // HISTORIAL DE INCIDENCIA
    // =========================================
    //
    // Verifica primero que el usuario
    // tenga permiso para consultar
    // la incidencia.
    //
    // =========================================

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialIncidencia>>
    listarHistorial(
            @PathVariable Long id,
            Authentication authentication) {

        String email =
                authentication.getName();

        String rol =
                authentication
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
                        .replace("ROLE_", "");

        boolean permitido =
                incidenciaService
                        .puedeAccederIncidencia(
                                id,
                                email,
                                rol
                        );

        if (!permitido) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        List<HistorialIncidencia> historial =
                historialIncidenciaService
                        .listarPorIncidencia(id);

        return ResponseEntity.ok(
                historial
        );
    }

}