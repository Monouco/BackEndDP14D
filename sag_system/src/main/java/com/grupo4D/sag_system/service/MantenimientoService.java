package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Mantenimiento;
import com.grupo4D.sag_system.repository.MantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MantenimientoService{
    @Autowired
    MantenimientoRepository mantenimientoRepository;

    public Mantenimiento guardarMantenimiento(Mantenimiento mantenimiento){
        return mantenimientoRepository.save(mantenimiento);
    }

    public Mantenimiento guardarMantenimientoNuevo(Mantenimiento mantenimiento){
        return mantenimientoRepository.save(mantenimiento);
    }

    public ArrayList<Mantenimiento> listarMantenimientos() {
        return (ArrayList<Mantenimiento>) mantenimientoRepository.findAll();
    }

}
