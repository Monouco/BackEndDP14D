package com.grupo4D.sag_system.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Fecha {

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime fecha;

    private double velocidad;

    private int tipo;

    public Fecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public Fecha(){

    }

    public Fecha(double velocidad){
        this.velocidad = velocidad;
    }

    public Fecha(LocalDateTime fecha, double velocidad){
        this.fecha = fecha;
        this.velocidad = velocidad;
    }

    public Fecha(double velocidad, int tipo) {
        this.velocidad = velocidad;
        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int pedido) {
        this.tipo = pedido;
    }
}
