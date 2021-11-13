package com.grupo4D.sag_system.service;


import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.RutaXPedido;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.PedidoRepository;
import com.grupo4D.sag_system.repository.NodoRepository;
import com.grupo4D.sag_system.repository.RutaXPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class PedidoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    NodoRepository nodoRepository;
    @Autowired
    RutaXPedidoRepository rutaXPedidoRepository;

    public Pedido guardarPedido(Pedido pedido){
        return pedidoRepository.save(pedido);
    }

    public Pedido guardarPedidoNuevo(Pedido pedido){
        pedido.setEstadoPedido("Nuevo");
        Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(pedido.getNodo().getCoordenadaX(), pedido.getNodo().getCoordenadaY());
        pedido.getNodo().setId(nodo.getId());
        pedido.setTipo(1); // 1 es simulacion dia a dia
        return pedidoRepository.save(pedido);
    }

    public ArrayList<Pedido> listarPedidos() {
        return (ArrayList<Pedido>) pedidoRepository.listarPedidos();
    }

    public ArrayList<Pedido> listarPedidosSinAsignar() {
        String estado = "Nuevo";
        return (ArrayList<Pedido>) pedidoRepository.findPedidosByEstadoPedido(estado);
    }

    public ArrayList<Pedido> listarPedidosTemp(){
        return pedidoRepository.listarPedidosDisponibles(LocalDateTime.now(StaticValues.zoneId), 1);
    }

    public ArrayList<Pedido> listarPedidosDisponibles(LocalDateTime date, int tipo){
        ArrayList<Pedido> pedidos = pedidoRepository.listarPedidosDisponibles(date, tipo);
        ArrayList<RutaXPedido> rutaXPedidos ;
        int i;
        double glp;
        for(i = 0; i<pedidos.size(); i++){
            Pedido p = pedidos.get(i);
            glp = 0;
            rutaXPedidos = rutaXPedidoRepository.listarRutaXPedido(p.getId());
            for (RutaXPedido rp:
                 rutaXPedidos) {
                glp = glp + rp.getCantidadGLPEnviado();
            }
            p.setGlpProgramado(p.getGlpProgramado() - glp);
        }
        return pedidos;
    }

}