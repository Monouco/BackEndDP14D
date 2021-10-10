package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Planta")

public class Planta {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idPlanta")
    private int id;
    //Resto de atributos

    @Column(name="capacidadGLP")
    private double capacidadGLP;

    @Column(name="glpDisponible")
    private double glpDisponible;

    @ManyToOne @JoinColumn(name="idNodo")
    private Nodo nodo;

    @Column(name="activo")
    private boolean activo = true;
}
