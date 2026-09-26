package com.mesaayuda.controller;

import com.mesaayuda.dto.ReporteResumenResponse;
import com.mesaayuda.service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mesaayuda.dto.ReportePrioridadResponse;
import com.mesaayuda.dto.ReporteTecnicoResponse;
import com.mesaayuda.dto.ReporteTipoResponse;
import com.mesaayuda.dto.ReporteIncidenciaRecienteResponse;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "http://localhost:4200")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(
            ReporteService reporteService) {

        this.reporteService = reporteService;
    }

    @GetMapping("/resumen")
    public ResponseEntity<ReporteResumenResponse>
    obtenerResumen() {

        return ResponseEntity.ok(
                reporteService.obtenerResumen()
        );
    }
    @GetMapping("/por-prioridad")
    public ResponseEntity<List<ReportePrioridadResponse>>
    obtenerPorPrioridad() {

        return ResponseEntity.ok(
                reporteService.obtenerPorPrioridad()
        );
    }
    @GetMapping("/por-tecnico")
    public ResponseEntity<List<ReporteTecnicoResponse>>
    obtenerPorTecnico() {

        return ResponseEntity.ok(
                reporteService.obtenerPorTecnico()
        );
    }
    @GetMapping("/por-tipo")
    public ResponseEntity<List<ReporteTipoResponse>>
    obtenerPorTipo() {

        return ResponseEntity.ok(
                reporteService.obtenerPorTipo()
        );
    }
    @GetMapping("/recientes")
    public ResponseEntity<List<ReporteIncidenciaRecienteResponse>>
    obtenerIncidenciasRecientes() {

        return ResponseEntity.ok(
                reporteService.obtenerIncidenciasRecientes()
        );
    }
}