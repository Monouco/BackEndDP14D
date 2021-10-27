package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Bloqueo;
import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.repository.BloqueoRepository;
import com.grupo4D.sag_system.repository.NodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class BloqueoService{
    @Autowired
    BloqueoRepository camionRepository;

    @Autowired
    NodoRepository nodoRepository;

    public Bloqueo guardarBloqueo(Bloqueo bloqueo){
        return camionRepository.save(bloqueo);
    }

    public Bloqueo guardarBloqueoNuevo(Bloqueo bloqueo){
        return camionRepository.save(bloqueo);
    }

    public ArrayList<Bloqueo> listarBloqueos() {
        ArrayList<Bloqueo> bloqueos = camionRepository.listarPedidosActuales(LocalDateTime.now());
        int i;
        for(i = 0; bloqueos.size()> i; i++){
            Bloqueo bloqueo = bloqueos.get(i);
            Nodo nodoBloqueado = nodoRepository.findNodoById(bloqueo.getNodo().getId());
            bloqueo.setNodo(nodoBloqueado);
        }
        return bloqueos;
    }

}

