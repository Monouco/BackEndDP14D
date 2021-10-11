package com.grupo4D.sag_system.model;

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

    @Column(name="fechaPedido")
    private LocalDateTime fechaPedido;

    @Column(name="fechaEntrega")
    private LocalDateTime fechaEntrega;

    @Column(name="estadoPedido")
    private String estadoPedido;

    @ManyToOne @JoinColumn(name="idNodo")
    private Nodo nodo;

    @Column(name="activo")
    private boolean activo = true;

    public Pedido(int id, int plazoEntrega, double cantidadGLP, LocalDateTime fechaPedido, LocalDateTime fechaEntrega, String estadoPedido, Nodo nodo) {
        this.id = id;
        this.plazoEntrega = plazoEntrega;
        this.cantidadGLP = cantidadGLP;
        this.fechaPedido = fechaPedido;
        this.fechaEntrega = fechaEntrega;
        this.estadoPedido = estadoPedido;
        this.nodo = nodo;
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
}
