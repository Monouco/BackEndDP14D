package com.grupo4D.sag_system.model.response;

import java.util.ArrayList;

public class HojaRutaFront {
    ArrayList<NodoHojaRutaFront> nodos;


    public HojaRutaFront(){
        nodos = new ArrayList<>();
    }

    public ArrayList<NodoHojaRutaFront> getNodos() {
        return nodos;
    }

    public void setNodos(ArrayList<NodoHojaRutaFront> nodos) {
        this.nodos = nodos;
    }

}
