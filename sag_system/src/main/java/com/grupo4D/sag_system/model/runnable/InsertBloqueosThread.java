package com.grupo4D.sag_system.model.runnable;

import com.grupo4D.sag_system.model.Bloqueo;
import com.grupo4D.sag_system.model.NodoXBloqueo;
import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.BloqueoRepository;
import com.grupo4D.sag_system.repository.NodoXBloqueoRepository;
import com.grupo4D.sag_system.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope("prototype")
public class InsertBloqueosThread implements Runnable{
    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    BloqueoRepository bloqueoRepository;

    @Autowired
    NodoXBloqueoRepository nodoXBloqueoRepository;

    private ArrayList<Bloqueo> bloqueos;
    private ArrayList<NodoXBloqueo> nodos;
    private int type;

    @Override
    public void run(){
        this.bloqueos = StaticValues.roadBlocks;
        this.nodos = StaticValues.blockedNodes;

        System.out.println("Bloqueos por insertar " + type + ": " + this.bloqueos.size());

        bloqueoRepository.saveAll(this.bloqueos);
        nodoXBloqueoRepository.saveAll(this.nodos);
    }
}
