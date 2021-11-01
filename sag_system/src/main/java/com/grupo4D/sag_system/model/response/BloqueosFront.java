package com.grupo4D.sag_system.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo4D.sag_system.model.Nodo;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BloqueosFront {
    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime endDate;
    private ArrayList<NodoFront> path;

    public BloqueosFront() {
        this.path = new ArrayList<>();
    }

    public BloqueosFront(LocalDateTime startDate, LocalDateTime endDate, ArrayList<NodoFront> path){
        this.startDate = startDate;
        this.endDate = endDate;
        this.path = path;
    }

    public BloqueosFront(String startDate, String endDate, ArrayList<NodoFront> path){
        //this.startDate = startDate;
        //this.endDate = endDate;
        this.path = path;
    }

    public BloqueosFront(int id, LocalDateTime startDate, LocalDateTime endDate, ArrayList<NodoFront> path) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.path = path;
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

    public ArrayList<NodoFront> getPath() {
        return path;
    }

    public void setPath(ArrayList<NodoFront> path) {
        this.path = path;
    }
}
