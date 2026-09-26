package com.mesaayuda.repository;

import com.mesaayuda.entity.AccionIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccionIncidenciaRepository
        extends JpaRepository<AccionIncidencia, Long> {

    List<AccionIncidencia> findByIncidenciaId(
            Long incidenciaId
    );

    boolean existsByIncidenciaId(
            Long incidenciaId
    );
}