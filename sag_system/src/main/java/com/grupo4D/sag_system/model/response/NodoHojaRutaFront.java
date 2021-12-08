package com.grupo4D.sag_system.model.response;

public class NodoHojaRutaFront {
    String inicio;
    String llegada;
    String tipo;

    public NodoHojaRutaFront(String inicio, String llegada, String tipo) {
        this.inicio = inicio;
        this.llegada = llegada;
        this.tipo = tipo;
    }

    public NodoHojaRutaFront() {
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getLlegada() {
        return llegada;
    }

    public void setLlegada(String llegada) {
        this.llegada = llegada;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
