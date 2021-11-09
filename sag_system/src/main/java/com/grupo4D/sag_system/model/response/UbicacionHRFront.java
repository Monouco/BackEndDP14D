package com.grupo4D.sag_system.model.response;

public class UbicacionHRFront {
    private int x;
    private int y;

    public UbicacionHRFront() {
    }

    public UbicacionHRFront(int x, int y) {
        this.x = x;
        this.y = y;
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
}
