package com.grupo4D.sag_system.model.response;

import java.time.LocalDateTime;
import java.util.ArrayList;

    public class RutaFront {
    private int id;
    private LocalDateTime startDate;
    private ArrayList<NodoFront> path;

    public RutaFront(int id, LocalDateTime startDate, ArrayList<NodoFront> path) {
        this.id = id;
        this.startDate = startDate;
        this.setPath(path);
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

    public ArrayList<NodoFront> getPath() {
        return path;
    }

    public void setPath(ArrayList<NodoFront> path) {
        this.path = path;
    }
}
