package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Usuario")

public class Usuario {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idUsuario")
    private int id;
    //Resto de atributos

    @Column(name="nombreUsuario")
    private String nombre;

    @Column(name="apellidoPaterno")
    private String apellidoPaterno;

    @Column(name="apellidoMaterno")
    private String apellidoMaterno;

    @Column(name="correo")
    private String correo;

    @Column(name="clave")
    private String clave;

    @Column(name="activo")
    private boolean activo = true;
}
