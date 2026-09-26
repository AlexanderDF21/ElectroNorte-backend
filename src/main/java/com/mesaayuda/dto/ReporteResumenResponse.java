package com.mesaayuda.dto;

public class ReporteResumenResponse {

    private long total;
    private long registradas;
    private long enRevision;
    private long asignadas;
    private long enAtencion;
    private long resueltas;
    private long cerradas;
    private long canceladas;

    public ReporteResumenResponse() {
    }

    public ReporteResumenResponse(
            long total,
            long registradas,
            long enRevision,
            long asignadas,
            long enAtencion,
            long resueltas,
            long cerradas,
            long canceladas) {

        this.total = total;
        this.registradas = registradas;
        this.enRevision = enRevision;
        this.asignadas = asignadas;
        this.enAtencion = enAtencion;
        this.resueltas = resueltas;
        this.cerradas = cerradas;
        this.canceladas = canceladas;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getRegistradas() {
        return registradas;
    }

    public void setRegistradas(long registradas) {
        this.registradas = registradas;
    }

    public long getEnRevision() {
        return enRevision;
    }

    public void setEnRevision(long enRevision) {
        this.enRevision = enRevision;
    }

    public long getAsignadas() {
        return asignadas;
    }

    public void setAsignadas(long asignadas) {
        this.asignadas = asignadas;
    }

    public long getEnAtencion() {
        return enAtencion;
    }

    public void setEnAtencion(long enAtencion) {
        this.enAtencion = enAtencion;
    }

    public long getResueltas() {
        return resueltas;
    }

    public void setResueltas(long resueltas) {
        this.resueltas = resueltas;
    }

    public long getCerradas() {
        return cerradas;
    }

    public void setCerradas(long cerradas) {
        this.cerradas = cerradas;
    }

    public long getCanceladas() {
        return canceladas;
    }

    public void setCanceladas(long canceladas) {
        this.canceladas = canceladas;
    }
}