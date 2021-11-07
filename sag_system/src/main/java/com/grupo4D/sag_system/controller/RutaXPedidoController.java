package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.RutaXPedido;
import com.grupo4D.sag_system.service.RutaXPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rutaXPedido")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
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

//    @PostMapping("/listarPedidos1Ruta")
//    public ArrayList<RutaXPedido> listarPedidos1Ruta(@RequestBody int id){
//        return rutaXPedidoService.listarPedidosDe1Ruta(id);
//    }
}

