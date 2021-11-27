package com.grupo4D.sag_system.model.response;

import com.grupo4D.sag_system.model.Pedido;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ColapsoFront {
    private LocalDateTime fechaColapso;

    private int pedidosAtendidos;
    private int pedidosPorAtender;
    private ArrayList<CamionHRFront> hojaRuta;
    private ArrayList<Pedido> pedidosEnCola;
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

    public ArrayList<CamionHRFront> getHojaRuta() {
        return hojaRuta;
    }

    public void setHojaRuta(ArrayList<CamionHRFront> hojaRuta) {
        this.hojaRuta = hojaRuta;
    }

    public ArrayList<Pedido> getPedidosEnCola() {
        return pedidosEnCola;
    }

    public void setPedidosEnCola(ArrayList<Pedido> pedidosEnCola) {
        this.pedidosEnCola = pedidosEnCola;
    }

}
