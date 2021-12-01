package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Bloqueo;
import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.NodoXBloqueo;
import com.grupo4D.sag_system.model.response.BloqueosFront;
import com.grupo4D.sag_system.model.response.NodoFront;
import com.grupo4D.sag_system.model.runnable.InsertBloqueosThread;
import com.grupo4D.sag_system.model.runnable.InsertPedidosThread;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.BloqueoRepository;
import com.grupo4D.sag_system.repository.NodoRepository;
import com.grupo4D.sag_system.repository.NodoXBloqueoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Service
public class BloqueoService{
    @Autowired
    BloqueoRepository camionRepository;

    @Autowired
    NodoRepository nodoRepository;

    @Autowired
    NodoXBloqueoRepository nodoXBloqueoRepository;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext applicationContext;


    public Bloqueo guardarBloqueo(Bloqueo bloqueo){
        return camionRepository.save(bloqueo);
    }

    public Bloqueo guardarBloqueoNuevo(Bloqueo bloqueo){
        return camionRepository.save(bloqueo);
    }

    public ArrayList<BloqueosFront> listarBloqueos(int type, double velocidad) {
        ArrayList<Bloqueo> bloqueos = camionRepository.listarBloqueosActuales(LocalDateTime.now(StaticValues.zoneId), type, velocidad);
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

                //nodos.add(new NodoFront(nodoRepository.findNodoById(nodoBloqueo.getNodo().getId())));
                nodos.add(new NodoFront(nodoBloqueo.getNodo()));
            }

            bloqueoFront.setId(bloqueo.getId());
            bloqueoFront.setStartDate(bloqueo.getFechaInicio().minusNanos((long)(bloqueo.getDesfase() - bloqueo.getTiempoEspera() *(1 + 1/velocidad) *1000 )));
            //bloqueoFront.setEndDate(bloqueo.getFechaFin().minusNanos(bloqueo.getDesfase()));
            bloqueoFront.setEndDate(bloqueo.getFechaFin().minusNanos((long)(bloqueo.getDesfase() - bloqueo.getTiempoEspera() *(1 + 1/velocidad) *1000
                    - bloqueo.getDuracion()/velocidad)));
            bloqueoFront.setPath(nodos);

            response.add(bloqueoFront);
        }
        return response;
    }

    public ArrayList<BloqueosFront> registrarBloqueos ( ArrayList<BloqueosFront> bloqueosFront) throws InterruptedException {
        ArrayList<Bloqueo> bloqueos = new ArrayList<>();
        ArrayList<NodoXBloqueo> nodos =  new ArrayList<>();
        Nodo temp;

        for (BloqueosFront bloqueoFront:
             bloqueosFront) {
            Bloqueo bloqueo = new Bloqueo();
            bloqueo.setFechaInicio(bloqueoFront.getStartDate());
            bloqueo.setFechaFin(bloqueoFront.getEndDate());
            bloqueo.setDuracion(ChronoUnit.NANOS.between(bloqueoFront.getStartDate(),bloqueoFront.getEndDate()));
            bloqueo.setActivo(true);
            bloqueo.setVigente(true);
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

        //Lo mandamos a un thread
        StaticValues.roadBlocks = bloqueos;
        StaticValues.blockedNodes = nodos;

        InsertBloqueosThread insertPedidos = applicationContext.getBean(InsertBloqueosThread.class);

        taskExecutor.execute(insertPedidos);

        TimeUnit.SECONDS.sleep(3);

         /*
        camionRepository.saveAll(bloqueos);
        nodoXBloqueoRepository.saveAll(nodos);*/

        return bloqueosFront;
    }

}

