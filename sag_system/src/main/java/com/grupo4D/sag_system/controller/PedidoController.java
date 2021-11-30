package com.grupo4D.sag_system.controller;


import com.grupo4D.sag_system.model.Fecha;
import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.request.FechaFront;
import com.grupo4D.sag_system.model.request.PedidoFront;
import com.grupo4D.sag_system.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pedido")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/guardarPedido")
    public Pedido savePedido(@RequestBody Pedido pedidoModel){
        return pedidoService.guardarPedido(pedidoModel);
    }

    @PostMapping("/registrarPedidoNuevo")
    public Pedido registrarPedidoNuevo(@RequestBody PedidoFront pedidoFront){
        Pedido pedidoModel = new Pedido();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
        String fecha = pedidoFront.getFechaPedido()+"@"+pedidoFront.getHora();
        LocalDateTime date = LocalDateTime.parse(fecha, formatter);

        pedidoModel.setFechaLimite(date.plusHours(pedidoFront.getPlazoEntrega()));
        pedidoModel.setFechaPedido(date);

        pedidoModel.setPlazoEntrega(pedidoFront.getPlazoEntrega());
        pedidoModel.setCantidadGLP(pedidoFront.getCantidadGLP());

        Nodo nodo = new Nodo();
        nodo.setCoordenadaX(pedidoFront.getUbicacionX());
        nodo.setCoordenadaY(pedidoFront.getUbicacionY());
        pedidoModel.setNodo(nodo);

        return pedidoService.guardarPedidoNuevo(pedidoModel);
    }

    @PostMapping("/registrarListaPedidos")
    public ArrayList<Pedido> registrarListaPedidos(@RequestBody ArrayList<PedidoFront> pedidosFront){
        ArrayList<Pedido> lista = new ArrayList<>();
        for (PedidoFront pedidoFront: pedidosFront   ) {
            Pedido pedidoModel = new Pedido();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
            String fecha = pedidoFront.getFechaPedido();
            LocalDateTime date = LocalDateTime.parse(fecha, formatter);

            pedidoModel.setFechaLimite(date.plusHours(pedidoFront.getPlazoEntrega()));
            pedidoModel.setFechaPedido(date);

            pedidoModel.setPlazoEntrega(pedidoFront.getPlazoEntrega());
            pedidoModel.setCantidadGLP(pedidoFront.getCantidadGLP());

            Nodo nodo = new Nodo();
            nodo.setCoordenadaX(pedidoFront.getUbicacionX());
            nodo.setCoordenadaY(pedidoFront.getUbicacionY());
            pedidoModel.setNodo(nodo);
            lista.add(pedidoModel);
        }

        return pedidoService.guardarListaPedidos(lista);
    }


    @GetMapping("/listarPedidos")
    public List<Pedido> listarPedidos(){
        return pedidoService.listarPedidos();
    }

    @PostMapping("/tempPedido")
    public ArrayList<Pedido> listarTempPedido (){
        return pedidoService.listarPedidosTemp();
    }

    @RequestMapping(value = "/generarPedidosColapso",  method= RequestMethod.GET)
    public String generarPedidosColapso (HttpServletRequest request){
        return pedidoService.generarPedidosColapso(request);
    }

    @RequestMapping(value = "/descargarPedidosColapso", produces="application/zip",  method= RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> descargarPedidosColapso (HttpServletRequest request){
        return pedidoService.descargarPedidosColapso(request);
    }


}
