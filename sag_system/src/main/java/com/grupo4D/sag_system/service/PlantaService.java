package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Planta;
import com.grupo4D.sag_system.repository.PlantaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PlantaService{
    @Autowired
    PlantaRepository plantaRepository;

    public Planta guardarPlanta(Planta planta){
        return plantaRepository.save(planta);
    }

    public Planta guardarPlantaNueva(Planta planta){
        return plantaRepository.save(planta);
    }

    public ArrayList<Planta> listarPlantas() {
        return (ArrayList<Planta>) plantaRepository.findAll();
    }

}



