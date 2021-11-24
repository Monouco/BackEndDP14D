package com.grupo4D.sag_system.model.request;

public class PlantaFront {
    private int tipo;
    private double capacidadGLP;
    private int x;
    private int y;

    public PlantaFront(){

    }

    public PlantaFront(int tipo, double capacidadGLP, int x, int y) {
        this.tipo = tipo;
        this.capacidadGLP = capacidadGLP;
        this.x = x;
        this.y = y;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public double getCapacidadGLP() {
        return capacidadGLP;
    }

    public void setCapacidadGLP(double capacidadGLP) {
        this.capacidadGLP = capacidadGLP;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
