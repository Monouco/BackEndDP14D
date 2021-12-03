package com.grupo4D.sag_system.service;


import com.grupo4D.sag_system.model.Usuario;
import com.grupo4D.sag_system.model.request.LoginFront;
import com.grupo4D.sag_system.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.ArrayList;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario guardarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Usuario guardarUsuarioNuevo(Usuario usuario){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(usuario.getClave().getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (int i: hash) {
                hexString.append(String.format("%02x", i));
            }
            usuario.setActivo(true);
            usuario.setClave(hexString.toString());
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario login(LoginFront loginFront){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(loginFront.getPassword().getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (int i: hash) {
                hexString.append(String.format("%02x", i));
            }
            try{
                Usuario u = usuarioRepository.login(loginFront.getUsername());
                if (u == null){
                    return null;
                }
                if (hexString.toString().equals(u.getClave())){
                    return u;
                }else{
                    return null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public ArrayList<Usuario> listarUsuarios() {
        return (ArrayList<Usuario>) usuarioRepository.findAll();
    }



}

