package com.mesaayuda.service;

import com.mesaayuda.entity.AccionIncidencia;
import com.mesaayuda.entity.Incidencia;
import com.mesaayuda.entity.Usuario;
import com.mesaayuda.enums.Rol;
import com.mesaayuda.repository.AccionIncidenciaRepository;
import com.mesaayuda.repository.IncidenciaRepository;
import com.mesaayuda.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AccionIncidenciaService {

    private final AccionIncidenciaRepository accionIncidenciaRepository;
    private final IncidenciaRepository incidenciaRepository;
    private final UsuarioRepository usuarioRepository;

    public AccionIncidenciaService(
            AccionIncidenciaRepository accionIncidenciaRepository,
            IncidenciaRepository incidenciaRepository,
            UsuarioRepository usuarioRepository) {

        this.accionIncidenciaRepository = accionIncidenciaRepository;
        this.incidenciaRepository = incidenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public AccionIncidencia registrarAccion(
            Long incidenciaId,
            String tecnicoEmail,
            String descripcion) {

        Incidencia incidencia = incidenciaRepository
                .findById(incidenciaId)
                .orElseThrow(() ->
                        new RuntimeException("Incidencia no encontrada"));

        Usuario tecnico = usuarioRepository
                .findByEmail(tecnicoEmail)
                .orElseThrow(() ->
                        new RuntimeException("Técnico no encontrado"));

        if (tecnico.getRol() != Rol.TECNICO) {
            throw new RuntimeException(
                    "El usuario autenticado no es técnico"
            );
        }

        if (incidencia.getTecnico() == null) {
            throw new RuntimeException(
                    "La incidencia no tiene técnico asignado"
            );
        }

        if (!incidencia.getTecnico()
                .getId()
                .equals(tecnico.getId())) {

            throw new RuntimeException(
                    "La incidencia está asignada a otro técnico"
            );
        }

        if (descripcion == null || descripcion.isBlank()) {
            throw new RuntimeException(
                    "La descripción es obligatoria"
            );
        }

        AccionIncidencia accion = new AccionIncidencia();

        accion.setIncidencia(incidencia);
        accion.setTecnico(tecnico);
        accion.setDescripcion(descripcion);
        accion.setFechaHora(LocalDateTime.now(ZoneId.of("America/Lima")));

        return accionIncidenciaRepository.save(accion);
    }

    public List<AccionIncidencia> listarPorIncidencia(
            Long incidenciaId) {

        return accionIncidenciaRepository
                .findByIncidenciaId(incidenciaId);
    }
}