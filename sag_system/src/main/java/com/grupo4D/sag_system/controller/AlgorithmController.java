package com.grupo4D.sag_system.controller;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo4D.sag_system.model.Fecha;
import com.grupo4D.sag_system.model.ObjObtenerRutasSolucion;
import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.request.SimulacionFront;
import com.grupo4D.sag_system.model.response.*;
import com.grupo4D.sag_system.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;


@RestController
@RequestMapping("/algoritmo")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class AlgorithmController {
    @Autowired
    private AlgorithmService algoritmoService;

//    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
//    @PostMapping("/generarSolucion")
//    public ArrayList<RutaFront> generarSolucion(@RequestParam(name = "horaInicio") String horaInicio){
//        return algoritmoService.asignarPedidos(horaInicio);
//    }

    /*@JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @PostMapping("/generarSolucion")
    public ArrayList<RutaFront> generarSolucion(@RequestBody Fecha horaInicio){
        return algoritmoService.asignarPedidos(horaInicio);
    }*/

//    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
//    @PostMapping("/generarSolucion")
//    public ArrayList<RutaFront> generarSolucion(@RequestBody String horaInicio){
//        return algoritmoService.asignarPedidos(horaInicio);
//    }

    @PostMapping("/obtenerRutas")
    public RespuestaObtenerRutaFront obtenerRutas(@RequestBody Fecha obj){
        return algoritmoService.obtenerRutasSolucion(obj, obj.getVelocidad(), obj.getTipo());
    }

    @PostMapping("/simulacionTresDias")
    public ArrayList<Pedido> simulacionTresDias(@RequestBody SimulacionFront pedidos) throws InterruptedException {
        return algoritmoService.asignarPedidos3Dias(pedidos.getOrders(), pedidos.getSpeed());
    }

    @PostMapping("/obtenerHojaDeRuta")
    public ArrayList<CamionHRFront> obtenerHojaDeRuta(@RequestBody TipoSimulacionFront t){
        return algoritmoService.obtenerHojaDeRuta(t);
    }

    @PostMapping("/simulacionColapso")
    public ArrayList<Pedido> simulacionColapso(@RequestBody SimulacionFront pedidos) throws InterruptedException {
        return algoritmoService.asignarPedidosColapso(pedidos.getOrders(), pedidos.getSpeed());
    }

}
