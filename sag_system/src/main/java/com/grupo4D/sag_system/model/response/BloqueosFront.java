package com.grupo4D.sag_system.model.response;

import com.grupo4D.sag_system.model.Nodo;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class BloqueosFront {
    private int id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ArrayList<Nodo> path;

    public BloqueosFront() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<Nodo> getPath() {
        return path;
    }

    public void setPath(ArrayList<Nodo> path) {
        this.path = path;
    }
}
