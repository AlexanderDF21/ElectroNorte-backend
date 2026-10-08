package com.mesaayuda.service;

import com.mesaayuda.entity.HistorialIncidencia;
import com.mesaayuda.entity.Incidencia;
import com.mesaayuda.entity.Usuario;
import com.mesaayuda.repository.HistorialIncidenciaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class HistorialIncidenciaService {

    private final HistorialIncidenciaRepository historialRepository;

    public HistorialIncidenciaService(
            HistorialIncidenciaRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    public HistorialIncidencia registrar(
            Incidencia incidencia,
            Usuario usuario,
            String accion,
            String estadoAnterior,
            String estadoNuevo,
            String observacion) {

        HistorialIncidencia historial = new HistorialIncidencia();

        historial.setIncidencia(incidencia);
        historial.setUsuario(usuario);
        historial.setAccion(accion);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        historial.setFechaHora(LocalDateTime.now(ZoneId.of("America/Lima")));
        historial.setObservacion(observacion);

        return historialRepository.save(historial);
    }

    public List<HistorialIncidencia> listarPorIncidencia(Long incidenciaId) {
        return historialRepository
                .findByIncidenciaIdOrderByFechaHoraAsc(incidenciaId);
    }
}