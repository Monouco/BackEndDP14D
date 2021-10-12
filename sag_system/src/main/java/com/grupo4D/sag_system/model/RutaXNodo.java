package com.grupo4D.sag_system.model;

import javax.persistence.*;

@Entity
@Table(name="RutaXNodo")

public class RutaXNodo {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idRutaXNodo")
    private int id;
    //Resto de atributos

    @Column(name="secuencia")
    private int secuencia;

    @ManyToOne @JoinColumn(name="idRuta")
    private Ruta ruta;

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

    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
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
