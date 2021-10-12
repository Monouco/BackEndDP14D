package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="Nodo")

public class Nodo {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idNodo")
    private int id;

    //Resto de atributos

    @Column(name="coordenadX")
    private int plazoEntrega;

    @Column(name="coordenadaY")
    private double cantidadGLP;

    @Column(name="activo")
    private boolean activo = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlazoEntrega() {
        return plazoEntrega;
    }

    public void setPlazoEntrega(int plazoEntrega) {
        this.plazoEntrega = plazoEntrega;
    }

    public double getCantidadGLP() {
        return cantidadGLP;
    }

    public void setCantidadGLP(double cantidadGLP) {
        this.cantidadGLP = cantidadGLP;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
