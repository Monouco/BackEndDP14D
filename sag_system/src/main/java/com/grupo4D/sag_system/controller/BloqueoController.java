package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Bloqueo;
import com.grupo4D.sag_system.service.BloqueoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bloqueo")
//@CrossOrigin("*")
public class BloqueoController {
    @Autowired
    private BloqueoService bloqueoService;

    @PostMapping("/guardarBloqueo")
    public Bloqueo guardarBloqueo(@RequestBody Bloqueo bloqueoModel){
        return bloqueoService.guardarBloqueo(bloqueoModel);
    }

    @PostMapping("/registrarBloqueoNuevo")
    public Bloqueo registrarBloqueoNuevo(@RequestBody Bloqueo bloqueoModel){
        return bloqueoService.guardarBloqueoNuevo(bloqueoModel);
    }

    @GetMapping("/listarBloqueos")
    public List<Bloqueo> listarBloqueos(){
        return bloqueoService.listarBloqueos();
    }
}

