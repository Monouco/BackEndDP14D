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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
