package com.grupo4D.sag_system.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class MantenimientoFront {
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime fecha;
    private String tipo;
    private String numero;

    public MantenimientoFront(){

    }

    public MantenimientoFront(LocalDateTime fecha, String tipo, String numero) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.numero = numero;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

}
