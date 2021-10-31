package com.grupo4D.sag_system.model.response;

import java.util.ArrayList;

public class RespuestaObtenerRutaFront {
    private ArrayList<RespuestaRutaFront> routes;
    private int flag;

    public RespuestaObtenerRutaFront() {
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
}
