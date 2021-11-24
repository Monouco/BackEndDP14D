package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Planta;
import com.grupo4D.sag_system.model.request.PlantaFront;
import com.grupo4D.sag_system.service.PlantaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planta")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class PlantaController {
    @Autowired
    private PlantaService plantaService;

    @PostMapping("/guardarPlanta")
    public Planta guardarPlanta(@RequestBody Planta plantaModel){
        return plantaService.guardarPlanta(plantaModel);
    }

    @PostMapping("/registrarPlantaNueva")
    public Planta registrarPlantaNueva(@RequestBody Planta plantaModel){
        return plantaService.guardarPlantaNueva(plantaModel);
    }

    @GetMapping("/listarPlantas")
    public List<Planta> listarPlantas(){
        return plantaService.listarPlantas();
    }

    @PostMapping("/registrarPlanta")
    public Planta registrarPlanta(@RequestBody PlantaFront plantaModel){
        return plantaService.registrarPlanta(plantaModel);
    }

}

