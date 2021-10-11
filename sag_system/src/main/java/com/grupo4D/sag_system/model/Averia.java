package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Averia")


public class Averia {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idAveria")
    private int id;
    //Resto de atributos

    @Column(name="descripcion")
    private String descripcion;

    @Column(name="fechaIncidente")
    private LocalDateTime fechaIncidente;

    @ManyToOne @JoinColumn(name="idNodo")
    private Nodo ubicacion;

    @ManyToOne @JoinColumn(name="idCamion")
    private Camion camion;

    @OneToOne @JoinColumn(name="fidMantenimiento",nullable = true)
    private Mantenimiento mantenimiento;

    @Column(name="activo")
    private boolean activo = true;


}
