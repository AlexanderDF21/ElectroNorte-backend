package com.mesaayuda.dto;

public class ReporteTecnicoResponse {

    private Long tecnicoId;
    private String tecnicoNombre;
    private long cantidad;

    public ReporteTecnicoResponse() {
    }

    public ReporteTecnicoResponse(
            Long tecnicoId,
            String tecnicoNombre,
            long cantidad) {

        this.tecnicoId = tecnicoId;
        this.tecnicoNombre = tecnicoNombre;
        this.cantidad = cantidad;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public String getTecnicoNombre() {
        return tecnicoNombre;
    }

    public void setTecnicoNombre(String tecnicoNombre) {
        this.tecnicoNombre = tecnicoNombre;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long cantidad) {
        this.cantidad = cantidad;
    }
}