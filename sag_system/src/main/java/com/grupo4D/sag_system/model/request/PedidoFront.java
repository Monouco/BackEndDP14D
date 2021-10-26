package com.grupo4D.sag_system.model.request;

public class PedidoFront {

    private int id;
    private int plazoEntrega;
    private double cantidadGLP;
    private String fechaPedido;
    private String hora;
    private String estadoPedido;
    private int ubicacionX;
    private int ubicacionY;

    public PedidoFront() {
    }

    public PedidoFront(int id, int plazoEntrega, double cantidadGLP, String fechaPedido, String hora, String estadoPedido, int ubicacionX, int ubicacionY) {
        this.setId(id);
        this.setPlazoEntrega(plazoEntrega);
        this.setCantidadGLP(cantidadGLP);
        this.setFechaPedido(fechaPedido);
        this.setHora(hora);
        this.setEstadoPedido(estadoPedido);
        this.setUbicacionX(ubicacionX);
        this.setUbicacionY(ubicacionY);
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

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public int getUbicacionX() {
        return ubicacionX;
    }

    public void setUbicacionX(int ubicacionX) {
        this.ubicacionX = ubicacionX;
    }

    public int getUbicacionY() {
        return ubicacionY;
    }

    public void setUbicacionY(int ubicacionY) {
        this.ubicacionY = ubicacionY;
    }
}
