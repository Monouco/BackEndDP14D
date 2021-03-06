package com.grupo4D.sag_system.model.response;

import com.grupo4D.sag_system.model.Nodo;

public class NodoFront {
    private int x;
    private int y;
    private int pedido;

    public NodoFront(int x, int y, int pedido) {
        this.x = x;
        this.y = y;
        this.pedido = pedido;
    }

    public NodoFront(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public NodoFront(Nodo nodo){
        this.x = nodo.getCoordenadaX();
        this.y = nodo.getCoordenadaY();
    }

    public NodoFront() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPedido() {
        return pedido;
    }

    public void setPedido(int pedido) {
        this.pedido = pedido;
    }
}
