package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.TipoCamion;
import com.grupo4D.sag_system.service.TipoCamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipoCamion")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class TipoCamionController {
    @Autowired
    private TipoCamionService tipoCamionService;

    @PostMapping("/guardarTipoCamion")
    public TipoCamion guardarTipoCamion(@RequestBody TipoCamion tipoCamionModel){
        return tipoCamionService.guardarTipoCamion(tipoCamionModel);
    }

    @PostMapping("/registrarTipoCamionNuevo")
    public TipoCamion registrarTipoCamionNuevo(@RequestBody TipoCamion tipoCamionModel){
        return tipoCamionService.guardarTipoCamionNuevo(tipoCamionModel);
    }

    @GetMapping("/listarTiposCamion")
    public List<TipoCamion> listarTiposCamion(){
        return tipoCamionService.listarTiposCamion();
    }
}


