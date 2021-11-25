package com.grupo4D.sag_system.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class FechaFront {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime fecha;

    public FechaFront(LocalDateTime f) {
        this.fecha = f;
    }

    public FechaFront() {
    }

    public LocalDateTime getF() {
        return fecha;
    }

    public void setF(LocalDateTime f) {
        this.fecha = f;
    }
}
