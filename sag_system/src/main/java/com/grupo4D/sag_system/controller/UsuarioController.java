package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Usuario;
import com.grupo4D.sag_system.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
//@CrossOrigin("*")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/guardarUsuario")
    public Usuario guardarUsuario(@RequestBody Usuario usuarioModel){
        return usuarioService.guardarUsuario(usuarioModel);
    }

    @PostMapping("/registrarUsuarioNuevo")
    public Usuario registrarUsuarioNuevo(@RequestBody Usuario usuarioModel){
        return usuarioService.guardarUsuarioNuevo(usuarioModel);
    }

    @GetMapping("/listarUsuario")
    public List<Usuario> listarUsuarios(){
        return usuarioService.listarUsuarios();
    }
}


