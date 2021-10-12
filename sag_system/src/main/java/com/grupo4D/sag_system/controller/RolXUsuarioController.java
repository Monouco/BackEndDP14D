package com.grupo4D.sag_system.controller;


import com.grupo4D.sag_system.model.RolXUsuario;
import com.grupo4D.sag_system.service.RolXUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rolXUsuario")
//@CrossOrigin("*")
public class RolXUsuarioController {
    @Autowired
    private RolXUsuarioService rolXUsuarioService;

    @PostMapping("/guardarRolXUsuario")
    public RolXUsuario guardarRolXUsuario(@RequestBody RolXUsuario rolXUsuarioModel){
        return rolXUsuarioService.guardarRolXUsuario(rolXUsuarioModel);
    }

    @PostMapping("/registrarRolXUsuario")
    public RolXUsuario registrarRolXUsuarioNuevo(@RequestBody RolXUsuario rolXUsuarioModel){
        return rolXUsuarioService.guardarRolXUsuarioNuevo(rolXUsuarioModel);
    }

    @GetMapping("/listarRolesXUsuario")
    public List<RolXUsuario> listarRolesXUsuario(){
        return rolXUsuarioService.listarRolesXUsuario();
    }
}
