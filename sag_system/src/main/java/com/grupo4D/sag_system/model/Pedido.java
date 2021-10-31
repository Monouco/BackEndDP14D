package com.grupo4D.sag_system.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Pedido")

public class Pedido {
    //Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idPedido")
    private int id;

    //Resto de atributos

    @Column(name="plazoEntrega")
    private int plazoEntrega;

    @Column(name="cantidadGlp")
    private double cantidadGLP;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(name="fechaPedido")
    private LocalDateTime fechaPedido;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(name="fechaEntrega")
    private LocalDateTime fechaEntrega;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(name="fechaLimite")
    private LocalDateTime fechaLimite;

    @Column(name="estadoPedido")
    private String estadoPedido;

    @ManyToOne @JoinColumn(name="idNodo")
    private Nodo nodo;

    @Column(name = "tipo")
    private int tipo;

    @Column(name="activo")
    private boolean activo = true;

    public Pedido(int id, int plazoEntrega, double cantidadGLP, LocalDateTime fechaPedido, LocalDateTime fechaEntrega, String estadoPedido, Nodo nodo) {
        this.setId(id);
        this.setPlazoEntrega(plazoEntrega);
        this.setCantidadGLP(cantidadGLP);
        this.setFechaPedido(fechaPedido);
        this.setFechaEntrega(fechaEntrega);
        this.setEstadoPedido(estadoPedido);
        this.setNodo(nodo);
    }

    public Pedido(int id, int plazoEntrega, double cantidadGLP, LocalDateTime fechaPedido, String estadoPedido, Nodo nodo) {
        this.setId(id);
        this.setPlazoEntrega(plazoEntrega);
        this.setCantidadGLP(cantidadGLP);
        this.setFechaPedido(fechaPedido);
        this.setEstadoPedido(estadoPedido);
        this.setNodo(nodo);
    }

    public Pedido() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlazoEntrega() {
        return plazoEntrega;
    }

    public void setPlazoEntrega(int plazoEntrega) {
        this.plazoEntrega = plazoEntrega;
    }

    public double getCantidadGLP() {
        return cantidadGLP;
    }

    public void setCantidadGLP(double cantidadGLP) {
        this.cantidadGLP = cantidadGLP;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public Nodo getNodo() {
        return nodo;
    }

    public void setNodo(Nodo nodo) {
        this.nodo = nodo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDateTime fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

}
