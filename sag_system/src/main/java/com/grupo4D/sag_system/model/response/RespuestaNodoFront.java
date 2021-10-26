package com.grupo4D.sag_system.model.response;

import java.time.LocalDateTime;

public class RespuestaNodoFront {
    private int x;
    private int y;
    private int indexRoute;
    private LocalDateTime deliveryDate;
    private LocalDateTime leftDate; //deliveryDate + timeAttention

    public RespuestaNodoFront() {
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

    public int getIndexRoute() {
        return indexRoute;
    }

    public void setIndexRoute(int indexRoute) {
        this.indexRoute = indexRoute;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDateTime getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(LocalDateTime leftDate) {
        this.leftDate = leftDate;
    }
}
