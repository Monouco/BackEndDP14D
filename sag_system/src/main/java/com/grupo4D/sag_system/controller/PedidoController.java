package com.grupo4D.sag_system.controller;


import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
//@CrossOrigin("*")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/guardarPedido")
    public Pedido savePedido(@RequestBody Pedido pedidoModel){
        return pedidoService.guardarPedido(pedidoModel);
    }

    @PostMapping("/registrarPedidoNuevo")
    public Pedido registrarPedidoNuevo(@RequestBody Pedido pedidoModel){
        return pedidoService.guardarPedidoNuevo(pedidoModel);
    }

    @GetMapping("/pedido")
    public List<Pedido> listarPedido(){
        return pedidoService.listarPedidos();
    }
}
