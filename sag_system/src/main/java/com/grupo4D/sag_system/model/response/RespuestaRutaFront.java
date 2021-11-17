package com.grupo4D.sag_system.model.response;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RespuestaRutaFront {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int attentionTime;
    private double velocity;
    private ArrayList<RespuestaNodoFront> orders;
    private ArrayList<NodoFront> route;
    private int idCamion;
    private int active = 1;

    public RespuestaRutaFront() {
        orders = new ArrayList<>();
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getAttentionTime() {
        return attentionTime;
    }

    public void setAttentionTime(int timeAttention) {
        this.attentionTime = timeAttention;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public ArrayList<RespuestaNodoFront> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<RespuestaNodoFront> orders) {
        this.orders = orders;
    }

    public ArrayList<NodoFront> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<NodoFront> route) {
        this.route = route;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getIdCamion() {
        return idCamion;
    }

    public void setIdCamion(int idCamion) {
        this.idCamion = idCamion;
    }
}
