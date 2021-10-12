package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Averia")


public class Averia {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idAveria")
    private int id;
    //Resto de atributos

    @Column(name="descripcion")
    private String descripcion;

    @Column(name="fechaIncidente")
    private LocalDateTime fechaIncidente;

    @ManyToOne @JoinColumn(name="idNodo")
    private Nodo ubicacion;

    @ManyToOne @JoinColumn(name="idCamion")
    private Camion camion;

    @OneToOne @JoinColumn(name="fidMantenimiento",nullable = true)
    private Mantenimiento mantenimiento;

    @Column(name="activo")
    private boolean activo = true;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaIncidente() {
        return fechaIncidente;
    }

    public void setFechaIncidente(LocalDateTime fechaIncidente) {
        this.fechaIncidente = fechaIncidente;
    }

    public Nodo getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Nodo ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Camion getCamion() {
        return camion;
    }

    public void setCamion(Camion camion) {
        this.camion = camion;
    }

    public Mantenimiento getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
