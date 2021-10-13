package com.grupo4D.sag_system.service;


import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.repository.PedidoRepository;
import com.grupo4D.sag_system.repository.NodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PedidoService {
    @Autowired
    PedidoRepository pedidoRepository;
    NodoRepository nodoRepository;

    public Pedido guardarPedido(Pedido pedido){
        return pedidoRepository.save(pedido);
    }

    public Pedido guardarPedidoNuevo(Pedido pedido){
        pedido.setEstadoPedido("Nuevo");

//        if (nodoRepository.findIdNodoByCoordenadaXAndCoordenadaY(pedido.getNodo().getCoordenadaX(), pedido.getNodo().getCoordenadaY()) != null){
//            Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaY(pedido.getNodo().getCoordenadaX(), pedido.getNodo().getCoordenadaY());
//            pedido.getNodo().setId(nodo.getId());
//        }
        //Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaY(pedido.getNodo().getCoordenadaX(), pedido.getNodo().getCoordenadaY());
        //pedido.getNodo().setId(nodo.getId());


        return pedidoRepository.save(pedido);
    }

    public ArrayList<Pedido> listarPedidos() {
        return (ArrayList<Pedido>) pedidoRepository.findAll();
    }



}