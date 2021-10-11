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
    private double estado;

    @ManyToOne @JoinColumn(name="idTipoCamion")
    private TipoCamion tipoCamion;

    @Column(name="activo")
    private boolean activo = true;
}
