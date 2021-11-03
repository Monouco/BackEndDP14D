package com.grupo4D.sag_system.model.request;

public class CamionRegistrarFront {

    private int codigoCamion;
    private int tipoCamion;
    private double velocidadCamion;

    public CamionRegistrarFront() {
    }

    public CamionRegistrarFront(int codigoCamion, int tipoCamion, double velocidadCamion) {
        this.codigoCamion = codigoCamion;
        this.tipoCamion = tipoCamion;
        this.velocidadCamion = velocidadCamion;
    }

    public int getCodigoCamion() {
        return codigoCamion;
    }

    public void setCodigoCamion(int codigoCamion) {
        this.codigoCamion = codigoCamion;
    }

    public int getTipoCamion() {
        return tipoCamion;
    }

    public void setTipoCamion(int tipoCamion) {
        this.tipoCamion = tipoCamion;
    }

    public double getVelocidadCamion() {
        return velocidadCamion;
    }

    public void setVelocidadCamion(double velocidadCamion) {
        this.velocidadCamion = velocidadCamion;
    }
}
