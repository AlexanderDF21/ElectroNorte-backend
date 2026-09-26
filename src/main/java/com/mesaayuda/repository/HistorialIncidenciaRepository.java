package com.mesaayuda.repository;

import com.mesaayuda.entity.HistorialIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialIncidenciaRepository
        extends JpaRepository<HistorialIncidencia, Long> {

    List<HistorialIncidencia> findByIncidenciaIdOrderByFechaHoraAsc(Long incidenciaId);
}