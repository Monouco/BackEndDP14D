package com.grupo4D.sag_system.model;

import javax.persistence.*;

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

    @Column(name="activo")
    private boolean activo = true;
}
