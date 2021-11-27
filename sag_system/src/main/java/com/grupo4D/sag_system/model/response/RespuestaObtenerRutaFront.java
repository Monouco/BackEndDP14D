package com.grupo4D.sag_system.model.response;

import java.util.ArrayList;

public class RespuestaObtenerRutaFront {

    private double glpRestantePlanta2;
    private double glpRestantePlanta3;
    private ArrayList<RespuestaRutaFront> routes;
    private int flag;
    private ColapsoFront collapseInfo;

    public RespuestaObtenerRutaFront() {
    }

    public RespuestaObtenerRutaFront(double glpRestantePlanta2, double glpRestantePlanta3, ArrayList<RespuestaRutaFront> routes, int flag, ColapsoFront collapseInfo) {
        this.glpRestantePlanta2 = glpRestantePlanta2;
        this.glpRestantePlanta3 = glpRestantePlanta3;
        this.routes = routes;
        this.flag = flag;
        this.collapseInfo = collapseInfo;
    }

    public double getGlpRestantePlanta2() {
        return glpRestantePlanta2;
    }

    public void setGlpRestantePlanta2(double glpRestantePlanta2) {
        this.glpRestantePlanta2 = glpRestantePlanta2;
    }

    public double getGlpRestantePlanta3() {
        return glpRestantePlanta3;
    }

    public void setGlpRestantePlanta3(double glpRestantePlanta3) {
        this.glpRestantePlanta3 = glpRestantePlanta3;
    }

    public ArrayList<RespuestaRutaFront> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<RespuestaRutaFront> routes) {
        this.routes = routes;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public ColapsoFront getCollapseInfo() {
        return collapseInfo;
    }

    public void setCollapseInfo(ColapsoFront collapseInfo) {
        this.collapseInfo = collapseInfo;
    }
}
