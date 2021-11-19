package com.grupo4D.sag_system.model;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(name="fechaInicio")
    private LocalDateTime fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(name="fechaFin")
    private LocalDateTime fechaFin;

    @Column(name="desfase")
    private long desfase;

    @ManyToOne @JoinColumn(name="idNodo")
    private Nodo nodo;

    @Column(name = "tipo")
    private int tipo;

    @Column(name="duracion")
    private long duracion;

    @Column(name="vigente")
    private boolean vigente;

    @Column(name="activo")
    private boolean activo = true;


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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public long getDesfase() {
        return desfase;
    }

    public void setDesfase(long desfase) {
        this.desfase = desfase;
    }

    public long getDuracion() {
        return duracion;
    }

    public void setDuracion(long duracion) {
        this.duracion = duracion;
    }

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }

}