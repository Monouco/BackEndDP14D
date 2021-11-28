package com.grupo4D.sag_system.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Fecha1TipoFront {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;
    private int tipo;

    public Fecha1TipoFront() {
    }

    public LocalDateTime getF() {
        return fecha;
    }

    public Fecha1TipoFront(LocalDateTime f, int tipo) {
        this.fecha = f;
        this.tipo = tipo;
    }

    public void setF(LocalDateTime f) {
        this.fecha = f;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
