package com.mesaayuda.controller;

import com.mesaayuda.entity.TipoIncidencia;
import com.mesaayuda.service.TipoIncidenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-incidencia")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoIncidenciaController {

    private final TipoIncidenciaService tipoIncidenciaService;

    public TipoIncidenciaController(
            TipoIncidenciaService tipoIncidenciaService) {
        this.tipoIncidenciaService = tipoIncidenciaService;
    }

    @GetMapping
    public List<TipoIncidencia> listarTodos() {
        return tipoIncidenciaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoIncidencia> buscarPorId(
            @PathVariable Long id) {

        return tipoIncidenciaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TipoIncidencia crear(
            @RequestBody TipoIncidencia tipoIncidencia) {

        return tipoIncidenciaService.guardar(tipoIncidencia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoIncidencia> actualizar(
            @PathVariable Long id,
            @RequestBody TipoIncidencia tipoIncidencia) {

        try {
            return ResponseEntity.ok(
                    tipoIncidenciaService.actualizar(id, tipoIncidencia)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TipoIncidencia> desactivar(
            @PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                    tipoIncidenciaService.desactivar(id)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}