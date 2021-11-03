package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Camion;
import com.grupo4D.sag_system.repository.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CamionService{
    @Autowired
    CamionRepository camionRepository;

    public Camion guardarCamion(Camion camion){
        return camionRepository.save(camion);
    }

    public Camion guardarCamionNuevo(Camion camion){
        camion.setActivo(true);
        return camionRepository.save(camion);
    }

    public ArrayList<Camion> listarCamiones() {
        return (ArrayList<Camion>) camionRepository.findCamionsByActivoTrue();
    }

    public String  buscarCodigo1Camion(int id) {
        return (String) camionRepository.listarCodigo1Camion(id);
    }

    public void cambiarEstadoAveria(String estado,int id){
        camionRepository.cambiarEstadoCamion(estado,id);
    }



}

