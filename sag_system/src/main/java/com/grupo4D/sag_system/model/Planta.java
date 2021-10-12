package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Planta")

public class Planta {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idPlanta")
    private int id;
    //Resto de atributos

    @Column(name="capacidadGLP")
    private double capacidadGLP;

    @Column(name="glpDisponible")
    private double glpDisponible;

    @ManyToOne @JoinColumn(name="idNodo")
    private Nodo nodo;

    @Column(name="activo")
    private boolean activo = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCapacidadGLP() {
        return capacidadGLP;
    }

    public void setCapacidadGLP(double capacidadGLP) {
        this.capacidadGLP = capacidadGLP;
    }

    public double getGlpDisponible() {
        return glpDisponible;
    }

    public void setGlpDisponible(double glpDisponible) {
        this.glpDisponible = glpDisponible;
    }

    public Nodo getNodo() {
        return nodo;
    }

    public void setNodo(Nodo nodo) {
        this.nodo = nodo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
