package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Camion;
import com.grupo4D.sag_system.model.Fecha;
import com.grupo4D.sag_system.service.CamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/camion")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class CamionController {
    @Autowired
    private CamionService camionService;

    @PostMapping("/guardarCamion")
    public Camion guardarCamion(@RequestBody Camion camionModel){
        return camionService.guardarCamion(camionModel);
    }

    @PostMapping("/registrarCamionNuevo")
    public Camion registrarCamionNuevo(@RequestBody Camion camionModel){
        return camionService.guardarCamionNuevo(camionModel);
    }

    @GetMapping("/listarCamiones")
    public List<Camion> listarCamiones(@RequestBody Fecha fecha){
        return camionService.listarCamiones(fecha.getEstado());
    }
}

