package com.grupo4D.sag_system.model.request;

import com.grupo4D.sag_system.model.Pedido;

import java.util.ArrayList;

public class SimulacionFront {
    private int speed;
    private ArrayList<Pedido> orders;

    public SimulacionFront(int speed, ArrayList<Pedido> orders) {
        this.speed = speed;
        this.orders = orders;
    }

    public SimulacionFront() {
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ArrayList<Pedido> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Pedido> orders) {
        this.orders = orders;
    }
}
