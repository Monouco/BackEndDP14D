package com.grupo4D.sag_system.model.response;

public class ConsumoPetroleoNodoFront {
    private double consumo;
    private String fecha;

    public ConsumoPetroleoNodoFront(double consumo, String fecha) {
        this.consumo = consumo;
        this.fecha = fecha;
    }

    public ConsumoPetroleoNodoFront() {
    }

    public double getConsumo() {
        return consumo;
    }

    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
