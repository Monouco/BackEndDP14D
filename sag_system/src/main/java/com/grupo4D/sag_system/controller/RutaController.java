package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Ruta;
import com.grupo4D.sag_system.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ruta")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class RutaController {
    @Autowired
    private RutaService rutaService;

    @PostMapping("/guardarRuta")
    public Ruta guardarRuta(@RequestBody Ruta rutaModel){
        return rutaService.guardarRuta(rutaModel);
    }

    @PostMapping("/registrarRutaNueva")
    public Ruta registrarRutaNueva(@RequestBody Ruta rutaModel){
        return rutaService.guardarRutaNueva(rutaModel);
    }

    @GetMapping("/listarRutas")
    public List<Ruta> listarRutas(){
        return rutaService.listarRutas();
    }
}

