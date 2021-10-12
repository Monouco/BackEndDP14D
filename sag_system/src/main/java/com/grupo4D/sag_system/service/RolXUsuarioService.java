package com.grupo4D.sag_system.service;


import com.grupo4D.sag_system.model.RolXUsuario;
import com.grupo4D.sag_system.repository.RolXUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RolXUsuarioService{
    @Autowired
    RolXUsuarioRepository rolXUsuarioRepository;

    public RolXUsuario guardarRolXUsuario(RolXUsuario rolXUsuario){
        return rolXUsuarioRepository.save(rolXUsuario);
    }

    public RolXUsuario guardarRolXUsuarioNuevo(RolXUsuario rolXUsuario){
        return rolXUsuarioRepository.save(rolXUsuario);
    }

    public ArrayList<RolXUsuario> listarRolesXUsuario() {
        return (ArrayList<RolXUsuario>) rolXUsuarioRepository.findAll();
    }

}
