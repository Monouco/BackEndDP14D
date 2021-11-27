package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Camion;
import com.grupo4D.sag_system.model.TipoCamion;
import com.grupo4D.sag_system.model.request.CamionRegistrarFront;
import com.grupo4D.sag_system.repository.CamionRepository;
import com.grupo4D.sag_system.repository.TipoCamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CamionService{
    @Autowired
    CamionRepository camionRepository;

    @Autowired
    TipoCamionRepository tipoCamionRepository;

    public Camion guardarCamion(Camion camion){
        return camionRepository.save(camion);
    }

    public Camion guardarCamionNuevo(Camion camion){
        camion.setActivo(true);
        return camionRepository.save(camion);
    }

    public Camion editarCamion(CamionRegistrarFront camion){
        Camion camionModel = camionRepository.findCamionById(camion.getIdCamion());
        TipoCamion tipoCamion = tipoCamionRepository.findTipoCamionById(camion.getTipoCamion());

        camionModel.setTipoCamion(tipoCamion);
        camionModel.setCodigo(String.format("%02d",camion.getCodigoCamion()));
        camionModel.setEstado(camion.getEstado());
        camionModel.setVelocidad(camion.getVelocidadCamion());
        camionModel.setKilometraje(camion.getKilometraje());
        camionModel.setActivo(true);
        return camionRepository.save(camionModel);
    }

    public ArrayList<Camion> listarCamiones() {
        return (ArrayList<Camion>) camionRepository.findCamionsByActivoTrue();
    }

    public String  buscarCodigo1Camion(int id) {
        return (String) camionRepository.listarCodigo1Camion(id);
    }

    public void cambiarEstadoAveria(String estado,int id){
        camionRepository.cambiarEstadoCamion(estado,id);
    }

    public ArrayList<Camion> listarCamionesAveriados(int tipo){
        return (ArrayList<Camion>) camionRepository.listarCamionesTipo("Averiado", tipo);
    }



}

