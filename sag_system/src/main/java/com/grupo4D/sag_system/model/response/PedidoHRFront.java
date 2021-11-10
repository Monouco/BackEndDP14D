package com.grupo4D.sag_system.model.response;

public class PedidoHRFront {
    private int idPedido;
    private double cantidadGLP;
    private String horaLlegada;
    private String horaDeFinAtencion;
    private UbicacionHRFront ubicacion;

    public PedidoHRFront() {
    }


    public PedidoHRFront(int idPedido, double cantidadGLP, String horaLlegada, String horaDeFinAtencion, UbicacionHRFront ubicacion) {
        this.idPedido = idPedido;
        this.cantidadGLP = cantidadGLP;
        this.horaLlegada = horaLlegada;
        this.horaDeFinAtencion = horaDeFinAtencion;
        this.ubicacion = ubicacion;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public double getCantidadGLP() {
        return cantidadGLP;
    }

    public void setCantidadGLP(double cantidadGLP) {
        this.cantidadGLP = cantidadGLP;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public String getHoraDeFinAtencion() {
        return horaDeFinAtencion;
    }

    public void setHoraDeFinAtencion(String horaDeFinAtencion) {
        this.horaDeFinAtencion = horaDeFinAtencion;
    }

    public UbicacionHRFront getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(UbicacionHRFront ubicacion) {
        this.ubicacion = ubicacion;
    }
}
