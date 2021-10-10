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

    @Column(name="coordenadX")
    private int plazoEntrega;

    @Column(name="coordenadaY")
    private double cantidadGLP;

    @Column(name="activo")
    private boolean activo = true;
}
