package com.grupo4D.sag_system.model.response;

import java.time.LocalDateTime;

public class ColapsoFront {
    private LocalDateTime fechaColapso;

    private int pedidosAtendidos;
    private int pedidosPorAtender;
    //Hoja de rutas
    //Camiones necesarios
    public ColapsoFront(){

    }

    public LocalDateTime getFechaColapso() {
        return fechaColapso;
    }

    public void setFechaColapso(LocalDateTime fechaColapso) {
        this.fechaColapso = fechaColapso;
    }

    public int getPedidosAtendidos() {
        return pedidosAtendidos;
    }

    public void setPedidosAtendidos(int pedidosAtendidos) {
        this.pedidosAtendidos = pedidosAtendidos;
    }

    public int getPedidosPorAtender() {
        return pedidosPorAtender;
    }

    public void setPedidosPorAtender(int pedidosPorAtender) {
        this.pedidosPorAtender = pedidosPorAtender;
    }

}
