package com.grupo4D.sag_system.controller;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo4D.sag_system.model.response.RutaFront;
import com.grupo4D.sag_system.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;


@RestController
@RequestMapping("/algoritmo")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class AlgorithmController {
    @Autowired
    private AlgorithmService algoritmoService;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    @PostMapping("/generarSolucion")
    public ArrayList<RutaFront> generarSolucion(@RequestParam(name = "horaInicio") String horaInicio){
        return algoritmoService.asignarPedidos(horaInicio);
    }
//    public ArrayList<RutaFront> generarSolucion(LocalDateTime horaInicio){
//        return algoritmoService.asignarPedidos(horaInicio);
//    }

}
