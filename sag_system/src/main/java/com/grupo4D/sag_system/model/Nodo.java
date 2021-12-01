package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="Nodo")

public class Nodo {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idNodo")
    private int id;

    //Resto de atributos

    @Column(name="coordenadaX")
    private int coordenadaX;

    @Column(name="coordenadaY")
    private int coordenadaY;

    @Column(name="activo")
    private boolean activo = true;

    public Nodo(int i,int j){
        coordenadaX = i;
        coordenadaY = j;
    }

    public  Nodo(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(int coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public int getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(int coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getCoor(){
        return coordenadaX + "," + coordenadaY;
    }
}
