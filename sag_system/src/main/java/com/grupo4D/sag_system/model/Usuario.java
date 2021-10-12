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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
