package com.grupo4D.sag_system.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class FechaFront {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime fecha;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime fechaIni;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime fechaFin;

    public FechaFront(LocalDateTime f) {
        this.fecha = f;
    }

    public FechaFront() {
    }

    public FechaFront(LocalDateTime fechaIni, LocalDateTime fechaFin) {
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
    }

    public LocalDateTime getF() {
        return fecha;
    }

    public void setF(LocalDateTime f) {
        this.fecha = f;
    }

    public LocalDateTime getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(LocalDateTime fechaIni) {
        this.fechaIni = fechaIni;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

}
