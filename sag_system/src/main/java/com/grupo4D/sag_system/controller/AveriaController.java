package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Averia;
import com.grupo4D.sag_system.service.AveriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/averia")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class AveriaController {
    @Autowired
    private AveriaService averiaService;

    @PostMapping("/guardarAveria")
    public Averia guardarAveria(@RequestBody Averia averiaModel){
        return averiaService.guardarAveria(averiaModel);
    }

    @PostMapping("/registrarAveriaNueva")
    public Averia registrarAveriaNueva(@RequestBody Averia averiaModel){
        return averiaService.guardarAveriaNueva(averiaModel);
    }

    @GetMapping("/listarAverias")
    public List<Averia> listarAverias(){
        return averiaService.listarAverias();
    }
}
