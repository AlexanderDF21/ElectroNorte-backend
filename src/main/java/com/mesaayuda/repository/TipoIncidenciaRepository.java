package com.mesaayuda.repository;

import com.mesaayuda.entity.TipoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoIncidenciaRepository
        extends JpaRepository<TipoIncidencia, Long> {
}