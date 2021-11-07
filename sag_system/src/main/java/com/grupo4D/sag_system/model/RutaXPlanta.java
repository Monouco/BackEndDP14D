package com.grupo4D.sag_system.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="RutaXPlanta")

public class RutaXPlanta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idRutaXAlmacen")
    private int id;

    @Column(name="cantidadGlpRepostado")
    private double cantidadGLPRespostado;

    @Column(name="fechaLLegada")
    private LocalDateTime fechaLLegada;

    @Column(name = "secuencia")
    private int secuencia;

    @ManyToOne @JoinColumn(name="idRuta")
    private Ruta ruta;

    @ManyToOne @JoinColumn(name="idPlanta")
    private Planta planta;

    @Column(name="activo")
    private boolean activo = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCantidadGLPRespostado() {
        return cantidadGLPRespostado;
    }

    public void setCantidadGLPRespostado(double cantidadGLPRespostado) {
        this.cantidadGLPRespostado = cantidadGLPRespostado;
    }

    public LocalDateTime getFechaLLegada() {
        return fechaLLegada;
    }

    public void setFechaLLegada(LocalDateTime fechaLLegada) {
        this.fechaLLegada = fechaLLegada;
    }

    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}
