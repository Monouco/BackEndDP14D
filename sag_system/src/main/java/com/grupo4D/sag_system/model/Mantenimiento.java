package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Mantenimiento")

public class Mantenimiento {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idAveria")
    private int id;
    //Resto de atributos

    @Column(name="tipoMantenimiento")
    private String tipo;

    @Column(name="fechaEntrada")
    private LocalDateTime fechaEntrada;

    @Column(name="fechaSalida")
    private LocalDateTime fechaSalida;

    @ManyToOne @JoinColumn(name="idCamion")
    private Camion camion;

    @Column(name="activo")
    private boolean activo = true;
}
