package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.repository.NodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NodoService{
    @Autowired
    NodoRepository nodoRepository;

    public Nodo guardarNodo(Nodo nodo){
        return nodoRepository.save(nodo);
    }

    public Nodo guardarNodoNuevo(Nodo nodo){
        return nodoRepository.save(nodo);
    }

    public ArrayList<Nodo> listarNodos() {
        return (ArrayList<Nodo>) nodoRepository.findAll();
    }

    public String generarNodos(){
        for(int i = 0; i < 70; i++){
            for(int j = 0; j < 50; j++){
                Nodo nodo = new Nodo(i,j);
                nodoRepository.save(nodo);
            }
        }
        return "CREADOS";
    }

}

