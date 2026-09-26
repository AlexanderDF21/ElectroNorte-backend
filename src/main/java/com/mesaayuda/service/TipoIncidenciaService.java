package com.mesaayuda.service;

import com.mesaayuda.entity.TipoIncidencia;
import com.mesaayuda.repository.TipoIncidenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoIncidenciaService {

    private final TipoIncidenciaRepository tipoIncidenciaRepository;

    public TipoIncidenciaService(
            TipoIncidenciaRepository tipoIncidenciaRepository) {
        this.tipoIncidenciaRepository = tipoIncidenciaRepository;
    }

    public List<TipoIncidencia> listarTodos() {
        return tipoIncidenciaRepository.findAll();
    }

    public Optional<TipoIncidencia> buscarPorId(Long id) {
        return tipoIncidenciaRepository.findById(id);
    }

    public TipoIncidencia guardar(TipoIncidencia tipoIncidencia) {
        return tipoIncidenciaRepository.save(tipoIncidencia);
    }

    public TipoIncidencia actualizar(
            Long id,
            TipoIncidencia datosActualizados) {

        TipoIncidencia tipo = tipoIncidenciaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Tipo de incidencia no encontrado"));

        tipo.setNombre(datosActualizados.getNombre());
        tipo.setDescripcion(datosActualizados.getDescripcion());
        tipo.setEstado(datosActualizados.getEstado());

        return tipoIncidenciaRepository.save(tipo);
    }

    public TipoIncidencia desactivar(Long id) {

        TipoIncidencia tipo = tipoIncidenciaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Tipo de incidencia no encontrado"));

        tipo.setEstado(false);

        return tipoIncidenciaRepository.save(tipo);
    }
}