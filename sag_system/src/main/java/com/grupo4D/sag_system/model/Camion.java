package com.grupo4D.sag_system.model;

import javax.persistence.*;

@Entity
@Table(name="Camion")

public class Camion {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idCamion")
    private int id;
    //Resto de atributos

    @Column(name="codigoCamion")
    private String codigo;

    @Column(name="velocidad")
    private double velocidad;

    @Column(name="estado")
    private String estado;

    @ManyToOne @JoinColumn(name="idTipoCamion")
    private TipoCamion tipoCamion;

    @Column(name="activo")
    private boolean activo = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TipoCamion getTipoCamion() {
        return tipoCamion;
    }

    public void setTipoCamion(TipoCamion tipoCamion) {
        this.tipoCamion = tipoCamion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
