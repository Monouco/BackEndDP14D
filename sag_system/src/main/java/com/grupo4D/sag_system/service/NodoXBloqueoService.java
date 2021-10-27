package com.grupo4D.sag_system.service;


import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.NodoXBloqueo;
import com.grupo4D.sag_system.repository.NodoXBloqueoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NodoXBloqueoService {
    @Autowired
    NodoXBloqueoRepository nodoXBloqueoRepository;

    public NodoXBloqueo guardarNodo(NodoXBloqueo nodo){
        return nodoXBloqueoRepository.save(nodo);
    }

    public NodoXBloqueo guardarNodoNuevo(NodoXBloqueo nodo){
        return nodoXBloqueoRepository.save(nodo);
    }



}
