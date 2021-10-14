package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Mantenimiento;
import com.grupo4D.sag_system.service.MantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mantenimiento")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class MantenimientoController {
    @Autowired
    private MantenimientoService mantenimientoService;

    @PostMapping("/guardarMantenimiento")
    public Mantenimiento guardarMantenimiento(@RequestBody Mantenimiento mantenimientoModel){
        return mantenimientoService.guardarMantenimiento(mantenimientoModel);
    }

    @PostMapping("/registrarMantenimientoNuevo")
    public Mantenimiento registrarMantenimientoNuevo(@RequestBody Mantenimiento mantenimientoModel){
        return mantenimientoService.guardarMantenimientoNuevo(mantenimientoModel);
    }

    @GetMapping("/listarMantenimientos")
    public List<Mantenimiento> listarMantenimientos(){
        return mantenimientoService.listarMantenimientos();
    }
}
