package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Averia;
import com.grupo4D.sag_system.repository.AveriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AveriaService{
    @Autowired
    AveriaRepository averiaRepository;

    public Averia guardarAveria(Averia averia){
        return averiaRepository.save(averia);
    }

    public Averia guardarAveriaNueva(Averia averia){
        return averiaRepository.save(averia);
    }

    public ArrayList<Averia> listarAverias() {
        return (ArrayList<Averia>) averiaRepository.findAll();
    }

}
