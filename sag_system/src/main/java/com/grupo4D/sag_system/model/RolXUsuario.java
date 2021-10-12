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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
