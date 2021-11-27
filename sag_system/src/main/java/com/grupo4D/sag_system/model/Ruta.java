package com.grupo4D.sag_system.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Ruta")

public class Ruta {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idRuta")
    private int id;
    //Resto de atributos

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(name="fechaInicio")
    private LocalDateTime fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(name="fechaFin")
    private LocalDateTime fechaFin;

    @ManyToOne @JoinColumn(name="idCamion")
    private Camion camion;

    @Column(name="estadoRuta")
    private String estado;

    @Column(name="tipo")
    private int tipo;

    @Column(name="desfase")
    private long desfase;

    @Column(name="costoOperacion")
    private double costoOperacion;

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

    public Camion getCamion() {
        return camion;
    }

    public void setCamion(Camion camion) {
        this.camion = camion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public double getCostoOperacion() {
        return costoOperacion;
    }

    public void setCostoOperacion(double costoOperacion) {
        this.costoOperacion = costoOperacion;
    }

}
