package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Rol;
import com.grupo4D.sag_system.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rol")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class RolController {
    @Autowired
    private RolService rolService;

    @PostMapping("/guardarRol")
    public Rol guardarRol(@RequestBody Rol rolModel){
        return rolService.guardarRolNuevo(rolModel);
    }

    @PostMapping("/registrarRol")
    public Rol registrarRolNuevo(@RequestBody Rol rolModel){
        return rolService.guardarRolNuevo(rolModel);
    }

    @GetMapping("/listarRoles")
    public List<Rol> listarRoles(){
        return rolService.listarRoles();
    }
}


