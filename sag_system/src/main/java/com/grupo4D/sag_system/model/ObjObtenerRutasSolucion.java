package com.grupo4D.sag_system.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class ObjObtenerRutasSolucion {

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    private Fecha fecha;

    private double velocidad;

    public ObjObtenerRutasSolucion(LocalDateTime fecha, double velocidad) {
        this.fecha = new Fecha();
        this.fecha.setFecha(fecha);
        this.setVelocidad(velocidad);
    }

    public ObjObtenerRutasSolucion(){

    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }
}
