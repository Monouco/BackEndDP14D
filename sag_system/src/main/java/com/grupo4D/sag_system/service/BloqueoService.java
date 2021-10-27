package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Bloqueo;
import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.NodoXBloqueo;
import com.grupo4D.sag_system.model.response.BloqueosFront;
import com.grupo4D.sag_system.repository.BloqueoRepository;
import com.grupo4D.sag_system.repository.NodoRepository;
import com.grupo4D.sag_system.repository.NodoXBloqueoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class BloqueoService{
    @Autowired
    BloqueoRepository camionRepository;

    @Autowired
    NodoRepository nodoRepository;

    @Autowired
    NodoXBloqueoRepository nodoXBloqueoRepository;

    public Bloqueo guardarBloqueo(Bloqueo bloqueo){
        return camionRepository.save(bloqueo);
    }

    public Bloqueo guardarBloqueoNuevo(Bloqueo bloqueo){
        return camionRepository.save(bloqueo);
    }

    public ArrayList<BloqueosFront> listarBloqueos() {
        ArrayList<Bloqueo> bloqueos = camionRepository.listarBloqueosActuales(LocalDateTime.now());
        ArrayList<BloqueosFront> response = new ArrayList<>();
        int i;
        for(i = 0; bloqueos.size()> i; i++){
            BloqueosFront bloqueoFront = new BloqueosFront();
            Bloqueo bloqueo = bloqueos.get(i);
            ArrayList<NodoXBloqueo> nodoBloqueados = nodoXBloqueoRepository.listarNodosXBloqueo(bloqueo.getNodo().getId());
            //bloqueo.setNodo(nodoBloqueado);
            ArrayList<Nodo> nodos = new ArrayList<>();
            for (NodoXBloqueo nodoBloqueo:
                 nodoBloqueados) {
                nodos.add(nodoRepository.findNodoById(nodoBloqueo.getNodo().getId()));
            }

            bloqueoFront.setId(bloqueo.getId());
            bloqueoFront.setStartDate(bloqueo.getFechaInicio());
            bloqueoFront.setEndDate(bloqueo.getFechaFin());
            bloqueoFront.setPath(nodos);

            response.add(bloqueoFront);
        }
        return response;
    }

}

