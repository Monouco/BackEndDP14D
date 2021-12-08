package com.grupo4D.sag_system.model.response;

import java.util.ArrayList;

public class HojaRutaFront {
    ArrayList<String> nodos;
    public HojaRutaFront(){
        nodos = new ArrayList<>();
    }

    public ArrayList<String> getNodos() {
        return nodos;
    }

    public void setNodos(ArrayList<String> nodos) {
        this.nodos = nodos;
    }

}
