package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Ruta")

public class Ruta {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idRuta")
    private int id;
    //Resto de atributos

    @Column(name="fechaInicio")
    private LocalDateTime fechaInicio;

    @Column(name="fechaFin")
    private LocalDateTime fechaFin;

    @ManyToOne @JoinColumn(name="idCamion")
    private Camion camion;

    @Column(name="activo")
    private boolean activo = true;
}
