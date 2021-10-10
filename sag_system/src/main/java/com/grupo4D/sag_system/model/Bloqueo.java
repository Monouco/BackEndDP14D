package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Bloqueo")

public class Bloqueo {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idBloqueo")
    private int id;
    //Resto de atributos

    @Column(name="fechaInicio")
    private LocalDateTime fechaInicio;

    @Column(name="fechaFin")
    private LocalDateTime fechaFin;

    @ManyToOne @JoinColumn(name="idNodo")
    private Nodo nodo;

    @Column(name="activo")
    private boolean activo = true;
}