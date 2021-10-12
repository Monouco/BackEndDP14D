package com.grupo4D.sag_system.model;

import javax.persistence.*;

@Entity
@Table(name="TipoCamion")

public class TipoCamion {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idTipoCamion")
    private int id;
    //Resto de atributos

    @Column(name="abreviatura")
    private String abreviatura;

    @Column(name="pesoTara")
    private double pesoTara;

    @Column(name="capacidadGlp")
    private double capacidadGLP;

    @Column(name="capacidadPetroleo")
    private double capacidadPetroleo;

    @Column(name="pesoGlp")
    private double pesoGLP;

    @Column(name="activo")
    private boolean activo = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public double getPesoTara() {
        return pesoTara;
    }

    public void setPesoTara(double pesoTara) {
        this.pesoTara = pesoTara;
    }

    public double getCapacidadGLP() {
        return capacidadGLP;
    }

    public void setCapacidadGLP(double capacidadGLP) {
        this.capacidadGLP = capacidadGLP;
    }

    public double getCapacidadPetroleo() {
        return capacidadPetroleo;
    }

    public void setCapacidadPetroleo(double capacidadPetroleo) {
        this.capacidadPetroleo = capacidadPetroleo;
    }

    public double getPesoGLP() {
        return pesoGLP;
    }

    public void setPesoGLP(double pesoGLP) {
        this.pesoGLP = pesoGLP;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
