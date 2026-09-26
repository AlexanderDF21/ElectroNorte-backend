package com.mesaayuda.service;

import com.mesaayuda.dto.ReporteIncidenciaRecienteResponse;
import com.mesaayuda.dto.ReportePrioridadResponse;
import com.mesaayuda.dto.ReporteResumenResponse;
import com.mesaayuda.dto.ReporteTecnicoResponse;
import com.mesaayuda.dto.ReporteTipoResponse;
import com.mesaayuda.entity.Incidencia;
import com.mesaayuda.entity.TipoIncidencia;
import com.mesaayuda.entity.Usuario;
import com.mesaayuda.enums.EstadoIncidencia;
import com.mesaayuda.enums.PrioridadIncidencia;
import com.mesaayuda.enums.Rol;
import com.mesaayuda.repository.IncidenciaRepository;
import com.mesaayuda.repository.TipoIncidenciaRepository;
import com.mesaayuda.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteService {

    private final IncidenciaRepository incidenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoIncidenciaRepository tipoIncidenciaRepository;

    public ReporteService(
            IncidenciaRepository incidenciaRepository,
            UsuarioRepository usuarioRepository,
            TipoIncidenciaRepository tipoIncidenciaRepository) {

        this.incidenciaRepository = incidenciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoIncidenciaRepository = tipoIncidenciaRepository;
    }
    public ReporteResumenResponse obtenerResumen() {

        long total =
                incidenciaRepository.count();

        long registradas =
                incidenciaRepository.countByEstado(
                        EstadoIncidencia.REGISTRADA
                );

        long enRevision =
                incidenciaRepository.countByEstado(
                        EstadoIncidencia.EN_REVISION
                );

        long asignadas =
                incidenciaRepository.countByEstado(
                        EstadoIncidencia.ASIGNADA
                );

        long enAtencion =
                incidenciaRepository.countByEstado(
                        EstadoIncidencia.EN_ATENCION
                );

        long resueltas =
                incidenciaRepository.countByEstado(
                        EstadoIncidencia.RESUELTA
                );

        long cerradas =
                incidenciaRepository.countByEstado(
                        EstadoIncidencia.CERRADA
                );

        long canceladas =
                incidenciaRepository.countByEstado(
                        EstadoIncidencia.CANCELADA
                );

        return new ReporteResumenResponse(
                total,
                registradas,
                enRevision,
                asignadas,
                enAtencion,
                resueltas,
                cerradas,
                canceladas
        );
    }
    public List<ReportePrioridadResponse>
    obtenerPorPrioridad() {

        return List.of(

                new ReportePrioridadResponse(
                        "BAJA",
                        incidenciaRepository.countByPrioridad(
                                PrioridadIncidencia.BAJA
                        )
                ),

                new ReportePrioridadResponse(
                        "MEDIA",
                        incidenciaRepository.countByPrioridad(
                                PrioridadIncidencia.MEDIA
                        )
                ),

                new ReportePrioridadResponse(
                        "ALTA",
                        incidenciaRepository.countByPrioridad(
                                PrioridadIncidencia.ALTA
                        )
                ),

                new ReportePrioridadResponse(
                        "CRITICA",
                        incidenciaRepository.countByPrioridad(
                                PrioridadIncidencia.CRITICA
                        )
                )
        );
    }
    public List<ReporteTecnicoResponse>
    obtenerPorTecnico() {

        List<Usuario> tecnicos =
                usuarioRepository.findAll()
                        .stream()
                        .filter(usuario ->
                                usuario.getRol() == Rol.TECNICO
                        )
                        .filter(usuario ->
                                Boolean.TRUE.equals(
                                        usuario.getEstado()
                                )
                        )
                        .toList();

        List<ReporteTecnicoResponse> resultado =
                new ArrayList<>();

        for (Usuario tecnico : tecnicos) {

            long cantidad =
                    incidenciaRepository
                            .countByTecnicoId(
                                    tecnico.getId()
                            );

            resultado.add(
                    new ReporteTecnicoResponse(
                            tecnico.getId(),
                            tecnico.getNombre()
                                    + " "
                                    + tecnico.getApellido(),
                            cantidad
                    )
            );
        }

        return resultado;
    }
    public List<ReporteTipoResponse>
    obtenerPorTipo() {

        List<TipoIncidencia> tipos =
                tipoIncidenciaRepository.findAll();

        List<ReporteTipoResponse> resultado =
                new ArrayList<>();

        for (TipoIncidencia tipo : tipos) {

            long cantidad =
                    incidenciaRepository
                            .countByTipoIncidenciaId(
                                    tipo.getId()
                            );

            resultado.add(
                    new ReporteTipoResponse(
                            tipo.getId(),
                            tipo.getNombre(),
                            cantidad
                    )
            );
        }

        return resultado;
    }
    public List<ReporteIncidenciaRecienteResponse>
    obtenerIncidenciasRecientes() {

        List<Incidencia> incidencias =
                incidenciaRepository
                        .findTop10ByOrderByFechaRegistroDesc();

        return incidencias
                .stream()
                .map(incidencia ->
                        new ReporteIncidenciaRecienteResponse(
                                incidencia.getCodigo(),
                                incidencia.getTitulo(),
                                incidencia.getPrioridad(),
                                incidencia.getEstado(),
                                incidencia.getFechaRegistro()
                        )
                )
                .toList();
    }
}