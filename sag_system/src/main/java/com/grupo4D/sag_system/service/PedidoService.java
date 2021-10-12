package com.grupo4D.sag_system.service;


import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PedidoService {
    @Autowired
    PedidoRepository pedidoRepository;

    public Pedido guardarPedido(Pedido pedido){
        return pedidoRepository.save(pedido);
    }

    public Pedido guardarPedidoNuevo(Pedido pedido){
        pedido.setEstadoPedido("Nuevo");
        return pedidoRepository.save(pedido);
    }

    public ArrayList<Pedido> listarPedidos() {
        return (ArrayList<Pedido>) pedidoRepository.findAll();
    }



}