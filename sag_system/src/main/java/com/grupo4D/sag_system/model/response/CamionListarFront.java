package com.grupo4D.sag_system.model.response;

public class CamionListarFront {
    private int id;
    private String tipoCamion;
    private double taraCamion;
    private double capacidadPetroleo;
    private double capacidadGLP;
    private String estadoCamion;

    public CamionListarFront() {
    }

    public CamionListarFront(int id, String tipoCamion, double taraCamion, double capacidadPetroleo, double capacidadGLP, String estadoCamion) {
        this.setId(id);
        this.setTipoCamion(tipoCamion);
        this.setTaraCamion(taraCamion);
        this.setCapacidadPetroleo(capacidadPetroleo);
        this.setCapacidadGLP(capacidadGLP);
        this.setEstadoCamion(estadoCamion);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoCamion() {
        return tipoCamion;
    }

    public void setTipoCamion(String tipoCamion) {
        this.tipoCamion = tipoCamion;
    }

    public double getTaraCamion() {
        return taraCamion;
    }

    public void setTaraCamion(double taraCamion) {
        this.taraCamion = taraCamion;
    }

    public double getCapacidadPetroleo() {
        return capacidadPetroleo;
    }

    public void setCapacidadPetroleo(double capacidadPetroleo) {
        this.capacidadPetroleo = capacidadPetroleo;
    }

    public double getCapacidadGLP() {
        return capacidadGLP;
    }

    public void setCapacidadGLP(double capacidadGLP) {
        this.capacidadGLP = capacidadGLP;
    }

    public String getEstadoCamion() {
        return estadoCamion;
    }

    public void setEstadoCamion(String estadoCamion) {
        this.estadoCamion = estadoCamion;
    }
}
