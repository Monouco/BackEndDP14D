package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Usuario;
import com.grupo4D.sag_system.model.request.LoginFront;
import com.grupo4D.sag_system.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
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

    @PostMapping("/login")
    public Usuario login(@RequestBody LoginFront l){
        return usuarioService.login(l);
    }

    @GetMapping("/listarUsuarios")
    public List<Usuario> listarUsuarios(){
        return usuarioService.listarUsuarios();
    }
}


