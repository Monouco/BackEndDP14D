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

    @Column(name="cantidadGLP")
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

}
