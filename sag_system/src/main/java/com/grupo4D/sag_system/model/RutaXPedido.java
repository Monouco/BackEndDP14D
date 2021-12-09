package com.grupo4D.sag_system.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="RutaXPedido")

public class RutaXPedido {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idRutaXPedido")
    private int id;
    //Resto de atributos

    @Column(name="costoOperacion")
    private double costoOperacion;

    @Column(name="cantidadGlpEnviado")
    private double cantidadGLPEnviado;

    @ManyToOne @JoinColumn(name="idRuta")
    private Ruta ruta;

    @ManyToOne @JoinColumn(name="idPedido")
    private Pedido pedido;

    @Column(name = "secuencia")
    private int secuencia;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="fechaEntrega")
    private LocalDateTime fechaEntrega;

    @Column(name="activo")
    private boolean activo = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCostoOperacion() {
        return costoOperacion;
    }

    public void setCostoOperacion(double costoOperacion) {
        this.costoOperacion = costoOperacion;
    }

    public double getCantidadGLPEnviado() {
        return cantidadGLPEnviado;
    }

    public void setCantidadGLPEnviado(double cantidadGLPEnviado) {
        this.cantidadGLPEnviado = cantidadGLPEnviado;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

}
