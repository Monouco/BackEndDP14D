package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Camion;
import com.grupo4D.sag_system.model.Mantenimiento;
import com.grupo4D.sag_system.model.request.MantenimientoFront;
import com.grupo4D.sag_system.repository.CamionRepository;
import com.grupo4D.sag_system.repository.MantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MantenimientoService{
    @Autowired
    MantenimientoRepository mantenimientoRepository;

    @Autowired
    CamionRepository camionRepository;

    public Mantenimiento guardarMantenimiento(Mantenimiento mantenimiento){
        return mantenimientoRepository.save(mantenimiento);
    }

    public Mantenimiento guardarMantenimientoNuevo(Mantenimiento mantenimiento){
        return mantenimientoRepository.save(mantenimiento);
    }

    public ArrayList<Mantenimiento> listarMantenimientos() {
        return  mantenimientoRepository.listarMantenimientos();
    }

    public ArrayList<MantenimientoFront> registrarMantenimientoPreventivo(ArrayList<MantenimientoFront> mantenimientosFront){
        ArrayList<Mantenimiento> mantenimientos = new ArrayList<>();
        for (MantenimientoFront temp:
             mantenimientosFront) {
            Mantenimiento mantenimiento = new Mantenimiento();
            mantenimiento.setTipoSimulacion(1);
            mantenimiento.setTipo("Preventivo");
            mantenimiento.setVigente(true);
            mantenimiento.setFechaEntrada(temp.getFecha());
            mantenimiento.setFechaSalida(temp.getFecha().plusDays(1));
            Camion camion = camionRepository.obtenerCamion(temp.getTipo(), temp.getNumero());
            mantenimiento.setCamion(camion);
            mantenimientos.add(mantenimiento);
        }
        mantenimientoRepository.saveAll(mantenimientos);
        return mantenimientosFront;
    }

}
