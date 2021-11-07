package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.RutaXPedido;
import com.grupo4D.sag_system.repository.RutaXPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RutaXPedidoService{
    @Autowired
    RutaXPedidoRepository rutaXPedidoRepository;

    public RutaXPedido guardarRutaXPedido(RutaXPedido rutaXPedido){
        return rutaXPedidoRepository.save(rutaXPedido);
    }

    public RutaXPedido guardarRutaXPedidoNuevo(RutaXPedido rutaXPedido){
        return rutaXPedidoRepository.save(rutaXPedido);
    }

    public ArrayList<RutaXPedido> listarRutasXPedido() {
        return (ArrayList<RutaXPedido>) rutaXPedidoRepository.findAll();
    }

//    public ArrayList<RutaXPedido> listarPedidosDe1Ruta(int id){
//        return (ArrayList<RutaXPedido>) rutaXPedidoRepository.findRutaXPedidosByIdRuta(id);
//    }



}

