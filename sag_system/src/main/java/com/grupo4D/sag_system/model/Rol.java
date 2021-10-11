package com.grupo4D.sag_system.model;

import javax.persistence.*;

@Entity
@Table(name="Rol")


public class Rol {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idRol")
    private int id;
    //Resto de atributos

    @Column(name="nombreRol")
    private int nombre;

    @Column(name="descripcionRol")
    private String descripcionRol;

    @Column(name="activo")
    private boolean activo = true;
}
