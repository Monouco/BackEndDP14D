package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Bloqueo;
import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.NodoXBloqueo;
import com.grupo4D.sag_system.model.response.BloqueosFront;
import com.grupo4D.sag_system.model.response.NodoFront;
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

    public ArrayList<BloqueosFront> listarBloqueos(int type, double velocidad) {
        ArrayList<Bloqueo> bloqueos = camionRepository.listarBloqueosActuales(LocalDateTime.now());
        ArrayList<BloqueosFront> response = new ArrayList<>();
        int i;
        for(i = 0; bloqueos.size()> i; i++){
            BloqueosFront bloqueoFront = new BloqueosFront();
            Bloqueo bloqueo = bloqueos.get(i);
            ArrayList<NodoXBloqueo> nodoBloqueados = nodoXBloqueoRepository.listarNodosXBloqueo(bloqueo.getId());
            //bloqueo.setNodo(nodoBloqueado);
            ArrayList<NodoFront> nodos = new ArrayList<>();
            for (NodoXBloqueo nodoBloqueo:
                 nodoBloqueados) {

                nodos.add(new NodoFront(nodoRepository.findNodoById(nodoBloqueo.getNodo().getId())));
            }

            bloqueoFront.setId(bloqueo.getId());
            bloqueoFront.setStartDate(bloqueo.getFechaInicio().minusNanos(bloqueo.getDesfase()));
            bloqueoFront.setEndDate(bloqueo.getFechaFin().minusNanos(bloqueo.getDesfase()));
            bloqueoFront.setPath(nodos);

            response.add(bloqueoFront);
        }
        return response;
    }

    public ArrayList<BloqueosFront> registrarBloqueos ( ArrayList<BloqueosFront> bloqueosFront){
        ArrayList<Bloqueo> bloqueos = new ArrayList<>();
        ArrayList<NodoXBloqueo> nodos =  new ArrayList<>();
        Nodo temp;

        for (BloqueosFront bloqueoFront:
             bloqueosFront) {
            Bloqueo bloqueo = new Bloqueo();
            bloqueo.setFechaInicio(bloqueoFront.getStartDate());
            bloqueo.setFechaFin(bloqueoFront.getEndDate());
            bloqueo.setActivo(true);
            bloqueo.setTipo(bloqueoFront.getType());
            //camionRepository.save(bloqueo);

            for (NodoFront nodoBloqueo:
                 bloqueoFront.getPath()) {
                temp = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(nodoBloqueo.getX(),nodoBloqueo.getY());
                NodoXBloqueo nodo = new NodoXBloqueo();
                nodo.setNodo(temp);
                nodo.setBloqueo(bloqueo);
                nodo.setActivo(true);
                nodos.add(nodo);
            }
            bloqueos.add(bloqueo);
        }
        camionRepository.saveAll(bloqueos);
        nodoXBloqueoRepository.saveAll(nodos);

        return bloqueosFront;
    }

}

