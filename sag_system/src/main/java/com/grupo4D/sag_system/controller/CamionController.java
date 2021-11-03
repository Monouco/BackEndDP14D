package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Camion;
import com.grupo4D.sag_system.model.Fecha;
import com.grupo4D.sag_system.model.TipoCamion;
import com.grupo4D.sag_system.model.request.CamionRegistrarFront;
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
    public Camion registrarCamionNuevo(@RequestBody CamionRegistrarFront camion){
        Camion camionModel = new Camion();
        TipoCamion tipoCamion = new TipoCamion();
        tipoCamion.setId(camion.getTipoCamion());


        camionModel.setTipoCamion(tipoCamion);
        camionModel.setCodigo(String.format("%02d",camion.getCodigoCamion()));
        camionModel.setEstadoSimulacion("Operativo");
        camionModel.setEstadoColapso("Operativo");
        camionModel.setEstado("Operativo");
        camionModel.setVelocidad(camion.getVelocidadCamion());

        return camionService.guardarCamionNuevo(camionModel);
    }

    @GetMapping("/listarCamiones")
    public List<Camion> listarCamiones(@RequestBody Fecha fecha){
        return camionService.listarCamiones(fecha.getEstado());
    }
}

