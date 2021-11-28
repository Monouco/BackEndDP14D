package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Averia;
import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.request.AveriaFront;
import com.grupo4D.sag_system.model.response.NodoFront;
import com.grupo4D.sag_system.repository.AveriaRepository;
import com.grupo4D.sag_system.repository.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AveriaService{
    @Autowired
    AveriaRepository averiaRepository;

    @Autowired
    CamionRepository camionRepository;

    public Averia guardarAveria(Averia averia){
        return averiaRepository.save(averia);
    }

    public Averia guardarAveriaNueva(Averia averia){
        return averiaRepository.save(averia);
    }

    public ArrayList<Averia> listarAverias() {
        return (ArrayList<Averia>) averiaRepository.findAll();
    }

    public ArrayList<AveriaFront> obtenerAverias(double velocidad, int tipo){
        ArrayList<AveriaFront> averias = new ArrayList<>();

        long nanos = 1000000000;
        long hora = 3600;
        long tiempo;
        long desfase;
        ArrayList<Averia> averiasActuales =  averiaRepository.listarAveriasActuales(tipo);

        for (Averia averiaActual:
             averiasActuales) {
            desfase = averiaActual.getDesfase();
            AveriaFront averia = new AveriaFront();
            averia.setStartDate(averiaActual.getFechaIncidente().minusNanos(desfase));
            tiempo = (long)(hora/velocidad * nanos);
            averia.setEndDate(averia.getStartDate().plusNanos(tiempo));
            averia.setIdCamion(averiaActual.getCamion().getId());
            averia.setCodigoCamion(camionRepository.listarCodigo1Camion(averia.getIdCamion()));
            Nodo nodoTempo =  averiaActual.getUbicacion();
            NodoFront nodo = new NodoFront();
            nodo.setX(nodoTempo.getCoordenadaX());
            nodo.setY(nodoTempo.getCoordenadaY());
            averia.setNode(nodo);
            averias.add(averia);
        }

        return averias;
    }

}
