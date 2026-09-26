package com.mesaayuda.dto;

public class ReportePrioridadResponse {

    private String prioridad;
    private long cantidad;

    public ReportePrioridadResponse() {
    }

    public ReportePrioridadResponse(
            String prioridad,
            long cantidad) {

        this.prioridad = prioridad;
        this.cantidad = cantidad;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long cantidad) {
        this.cantidad = cantidad;
    }
}