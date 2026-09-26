package com.mesaayuda.dto;

public class ReporteTipoResponse {

    private Long tipoId;
    private String tipoNombre;
    private long cantidad;

    public ReporteTipoResponse() {
    }

    public ReporteTipoResponse(
            Long tipoId,
            String tipoNombre,
            long cantidad) {

        this.tipoId = tipoId;
        this.tipoNombre = tipoNombre;
        this.cantidad = cantidad;
    }

    public Long getTipoId() {
        return tipoId;
    }

    public void setTipoId(Long tipoId) {
        this.tipoId = tipoId;
    }

    public String getTipoNombre() {
        return tipoNombre;
    }

    public void setTipoNombre(String tipoNombre) {
        this.tipoNombre = tipoNombre;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long cantidad) {
        this.cantidad = cantidad;
    }
}