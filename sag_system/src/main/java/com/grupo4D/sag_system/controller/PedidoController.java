package com.grupo4D.sag_system.controller;


import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
    @Autowired
    PedidoService pedidoService;

    @PostMapping("/guardarPedido")
    public Pedido savePedido(@RequestBody Pedido pedidoModel){
        return pedidoService.guardarPedido(pedidoModel);
    }

    @PostMapping("/registrarPedidoNuevo")
    public Pedido registrarPedidoNuevo(@RequestBody Pedido pedidoModel){
        return null;
    }


}
