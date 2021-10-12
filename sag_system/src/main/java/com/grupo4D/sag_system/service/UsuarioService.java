package com.grupo4D.sag_system.service;


import com.grupo4D.sag_system.model.Usuario;
import com.grupo4D.sag_system.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario guardarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Usuario guardarUsuarioNuevo(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public ArrayList<Usuario> listarUsuarios() {
        return (ArrayList<Usuario>) usuarioRepository.findAll();
    }



}

