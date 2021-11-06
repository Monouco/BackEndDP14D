package com.grupo4D.sag_system.model.response;

import java.util.ArrayList;

public class CamionHRFront {
    private String codigoCamion;
    private int numPedidos;
    private String horaSalida;
    private String horaLlegada;
    private double cantPetroleoActual;
    private ArrayList<PedidoHRFront> pedidos;

    public CamionHRFront() {
    }

    public CamionHRFront(String codigoCamion, int numPedidos, String horaSalida, String horaLlegada, double cantPetroleoActual, ArrayList<PedidoHRFront> pedidos) {
        this.setCodigoCamion(codigoCamion);
        this.setNumPedidos(numPedidos);
        this.setHoraSalida(horaSalida);
        this.setHoraLlegada(horaLlegada);
        this.setCantPetroleoActual(cantPetroleoActual);
        this.setPedidos(pedidos);
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
