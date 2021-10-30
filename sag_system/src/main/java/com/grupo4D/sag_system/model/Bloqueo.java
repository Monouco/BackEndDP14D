package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name="Bloqueo")

public class Bloqueo {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idBloqueo")
    private int id;
    //Resto de atributos

    @Column(name="fechaInicio")
    private LocalDateTime fechaInicio;

    @Column(name="fechaFin")
    private LocalDateTime fechaFin;

    @ManyToOne @JoinColumn(name="idNodo")
    private Nodo nodo;

    @Column(name="activo")
    private boolean activo = true;

    private ArrayList<NodoXBloqueo> nodos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
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