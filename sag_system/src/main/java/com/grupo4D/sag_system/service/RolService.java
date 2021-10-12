package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Rol;
import com.grupo4D.sag_system.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RolService{
    @Autowired
    RolRepository rolRepository;

    public Rol guardarRol(Rol rol){
        return rolRepository.save(rol);
    }

    public Rol guardarRolNuevo(Rol rol){
        return rolRepository.save(rol);
    }

    public ArrayList<Rol> listarRoles() {
        return (ArrayList<Rol>) rolRepository.findAll();
    }

}

