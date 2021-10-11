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
}
