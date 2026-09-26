package com.mesaayuda.repository;

import com.mesaayuda.entity.Incidencia;
import com.mesaayuda.enums.EstadoIncidencia;
import com.mesaayuda.enums.PrioridadIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncidenciaRepository
        extends JpaRepository<Incidencia, Long> {

    Optional<Incidencia> findByCodigo(String codigo);

    List<Incidencia> findByUsuarioEmail(String email);

    List<Incidencia> findByTecnicoEmail(String email);

    Optional<Incidencia> findByIdAndUsuarioEmail(
            Long id,
            String email
    );

    Optional<Incidencia> findByIdAndTecnicoEmail(
            Long id,
            String email
    );

    long countByEstado(
            EstadoIncidencia estado
    );

    long countByPrioridad(
            PrioridadIncidencia prioridad
    );

    long countByTecnicoId(
            Long tecnicoId
    );

    long countByTipoIncidenciaId(
            Long tipoIncidenciaId
    );

    List<Incidencia>
    findTop10ByOrderByFechaRegistroDesc();
}