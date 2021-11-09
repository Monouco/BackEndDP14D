package com.grupo4D.sag_system.model.response;

public class PedidoHRFront {
    private double cantidadGLP;
    private String horaLlegada;
    private String horaDeFinAtencion;
    private UbicacionHRFront ubicacion;

    public PedidoHRFront() {
    }

    public PedidoHRFront(double cantidadGLP, String horaLlegada, String horaDeFinAtencion, UbicacionHRFront ubicacion) {
        this.setCantidadGLP(cantidadGLP);
        this.setHoraLlegada(horaLlegada);
        this.setHoraDeFinAtencion(horaDeFinAtencion);
        this.setUbicacion(ubicacion);
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
