package com.grupo4D.sag_system.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class FechaFront {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime fecha;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime fechaIni;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime fechaFin;

    private int tipo = 1;

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

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

}
