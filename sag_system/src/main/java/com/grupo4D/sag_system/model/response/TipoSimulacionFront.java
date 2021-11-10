package com.grupo4D.sag_system.model.response;

public class TipoSimulacionFront {
    private int tipo;
    private int velocidad;

    public TipoSimulacionFront(int tipo, int velocidad) {
        this.tipo = tipo;
        this.velocidad = velocidad;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public TipoSimulacionFront() {
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
