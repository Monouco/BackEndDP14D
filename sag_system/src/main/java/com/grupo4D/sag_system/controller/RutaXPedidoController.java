package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.RutaXPedido;
import com.grupo4D.sag_system.service.RutaXPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rutaXPedido")
//@CrossOrigin("*")
public class RutaXPedidoController {
    @Autowired
    private RutaXPedidoService rutaXPedidoService;

    @PostMapping("/guardarRutaXPedido")
    public RutaXPedido guaradarRutaXPedido(@RequestBody RutaXPedido rutaXPedidoModel){
        return rutaXPedidoService.guardarRutaXPedido(rutaXPedidoModel);
    }

    @PostMapping("/registrarRutaXPedidoNueva")
    public RutaXPedido registrarRutaXPedidoNueva(@RequestBody RutaXPedido rutaXPedidoModel){
        return rutaXPedidoService.guardarRutaXPedidoNuevo(rutaXPedidoModel);
    }

    @GetMapping("/listarRutasXPedido")
    public List<RutaXPedido> listarRutaXPedido(){
        return rutaXPedidoService.listarRutasXPedido();
    }
}

