package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Planta;
import com.grupo4D.sag_system.model.request.PlantaFront;
import com.grupo4D.sag_system.repository.NodoRepository;
import com.grupo4D.sag_system.repository.PlantaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PlantaService{
    @Autowired
    PlantaRepository plantaRepository;

    @Autowired
    NodoRepository nodoRepository;

    public Planta guardarPlanta(Planta planta){
        return plantaRepository.save(planta);
    }

    public Planta guardarPlantaNueva(Planta planta){
        return plantaRepository.save(planta);
    }

    public ArrayList<Planta> listarPlantas() {
        return (ArrayList<Planta>) plantaRepository.listarPlantas();
    }

    public Planta registrarPlanta(PlantaFront plantaFront){
        double capacidad = (plantaFront.getTipo() == 1) ? 99999999 : plantaFront.getCapacidadGLP();
        Planta planta = new Planta();
        planta.setCapacidadGLP(capacidad);
        planta.setTipoPlanta(plantaFront.getTipo());
        planta.setNodo(nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(plantaFront.getX(), plantaFront.getY()));
        planta.setGlpDisponibleColapso(capacidad);
        planta.setGlpDisponible(capacidad);
        planta.setGlpDisponibleSimulacion(capacidad);
        planta.setActivo(true);
        plantaRepository.save(planta);
        return planta;
    }

}



