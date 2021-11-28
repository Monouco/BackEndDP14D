package com.grupo4D.sag_system.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

public class Fecha2TipoFront {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaFin;
    private int tipo;


    public Fecha2TipoFront() {
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Fecha2TipoFront(LocalDateTime fechaInicio, LocalDateTime fechaFin, int tipo) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
