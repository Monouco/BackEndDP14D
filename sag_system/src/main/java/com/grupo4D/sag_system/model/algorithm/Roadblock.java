package com.grupo4D.sag_system.model.algorithm;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Roadblock {
    private int diaIni;
    private int horaIni;
    private int minIni;
    private int diaFin;
    private int horaFin;
    private int minFin;
    private LocalDateTime fechaIni;
    private LocalDateTime fechaFin;
    private ArrayList<int []> nodes;
    private int[] nodo;


    public Roadblock() {
        nodes = new ArrayList<>();
    }

    public Roadblock(int diaIni, int horaIni, int minIni, int diaFin, int horaFin, int minFin) {
        this.diaIni = diaIni;
        this.horaIni = horaIni;
        this.minIni = minIni;
        this.diaFin = diaFin;
        this.horaFin = horaFin;
        this.minFin = minFin;
        nodes = new ArrayList<>();
    }

    public int getDiaIni() {
        return diaIni;
    }

    public void setDiaIni(int diaIni) {
        this.diaIni = diaIni;
    }

    public int getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(int horaIni) {
        this.horaIni = horaIni;
    }

    public int getMinIni() {
        return minIni;
    }

    public void setMinIni(int minIni) {
        this.minIni = minIni;
    }

    public int getDiaFin() {
        return diaFin;
    }

    public void setDiaFin(int diaFin) {
        this.diaFin = diaFin;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public int getMinFin() {
        return minFin;
    }

    public void setMinFin(int minFin) {
        this.minFin = minFin;
    }

    public ArrayList<int[]> getNodes() {
        return nodes;
    }

    public void appendNode(int x, int y){
        this.nodes.add(new int[]{x,y});
    }

    public LocalDateTime getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(LocalDateTime fechaIni) {
        this.fechaIni = fechaIni;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int[] getNodo() {
        return nodo;
    }

    public void setNodo(int[] nodo) {
        this.nodo = nodo;
    }
}
