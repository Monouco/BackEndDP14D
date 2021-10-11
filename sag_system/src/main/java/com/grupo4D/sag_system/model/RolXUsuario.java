package com.grupo4D.sag_system.model;

import javax.persistence.*;

@Entity
@Table(name="RolXUsuario")

public class RolXUsuario {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idRolXUsuario")
    private int id;
    //Resto de atributos

    @ManyToOne @JoinColumn(name="idUsuario")
    private Usuario usuario;

    @ManyToOne @JoinColumn(name="idRol")
    private Rol rol;

    @Column(name="activo")
    private boolean activo = true;
}
