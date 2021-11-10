package com.grupo4D.sag_system.model.response;

import java.util.ArrayList;

public class CamionHRFront {
    private int id;
    private String codigoCamion;
    private int numPedidos;
    private String horaSalida;
    private String horaLlegada;
    private double cantPetroleoActual;
    private double cantGlpActual;
    private ArrayList<PedidoHRFront> pedidos;

    public CamionHRFront() {
    }

    public CamionHRFront(int id, String codigoCamion, int numPedidos, String horaSalida, String horaLlegada, double cantPetroleoActual, double cantGlpActual, ArrayList<PedidoHRFront> pedidos) {
        this.id = id;
        this.codigoCamion = codigoCamion;
        this.numPedidos = numPedidos;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.cantPetroleoActual = cantPetroleoActual;
        this.cantGlpActual = cantGlpActual;
        this.pedidos = pedidos;
    }

    public double getCantGlpActual() {
        return cantGlpActual;
    }

    public void setCantGlpActual(double cantGlpActual) {
        this.cantGlpActual = cantGlpActual;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoCamion() {
        return codigoCamion;
    }

    public void setCodigoCamion(String codigoCamion) {
        this.codigoCamion = codigoCamion;
    }

    public int getNumPedidos() {
        return numPedidos;
    }

    public void setNumPedidos(int numPedidos) {
        this.numPedidos = numPedidos;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public double getCantPetroleoActual() {
        return cantPetroleoActual;
    }

    public void setCantPetroleoActual(double cantPetroleoActual) {
        this.cantPetroleoActual = cantPetroleoActual;
    }

    public ArrayList<PedidoHRFront> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<PedidoHRFront> pedidos) {
        this.pedidos = pedidos;
    }
}
