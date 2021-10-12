package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Bloqueo;
import com.grupo4D.sag_system.repository.BloqueoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BloqueoService{
    @Autowired
    BloqueoRepository camionRepository;

    public Bloqueo guardarBloqueo(Bloqueo bloqueo){
        return camionRepository.save(bloqueo);
    }

    public Bloqueo guardarBloqueoNuevo(Bloqueo bloqueo){
        return camionRepository.save(bloqueo);
    }

    public ArrayList<Bloqueo> listarBloqueos() {
        return (ArrayList<Bloqueo>) camionRepository.findAll();
    }

}

