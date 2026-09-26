package com.mesaayuda.dto;

import com.mesaayuda.enums.EstadoIncidencia;
import com.mesaayuda.enums.PrioridadIncidencia;

import java.time.LocalDateTime;

public class ReporteIncidenciaRecienteResponse {

    private String codigo;
    private String titulo;
    private PrioridadIncidencia prioridad;
    private EstadoIncidencia estado;
    private LocalDateTime fechaRegistro;

    public ReporteIncidenciaRecienteResponse() {
    }

    public ReporteIncidenciaRecienteResponse(
            String codigo,
            String titulo,
            PrioridadIncidencia prioridad,
            EstadoIncidencia estado,
            LocalDateTime fechaRegistro) {

        this.codigo = codigo;
        this.titulo = titulo;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public PrioridadIncidencia getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadIncidencia prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoIncidencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoIncidencia estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}