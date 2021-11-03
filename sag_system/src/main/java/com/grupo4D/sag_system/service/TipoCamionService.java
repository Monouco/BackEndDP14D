package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.TipoCamion;
import com.grupo4D.sag_system.repository.TipoCamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TipoCamionService{
    @Autowired
    TipoCamionRepository tipoCamionRepository;

    public TipoCamion guardarTipoCamion(TipoCamion tipoCamion){
        return tipoCamionRepository.save(tipoCamion);
    }

    public TipoCamion guardarTipoCamionNuevo(TipoCamion tipoCamion){
        return tipoCamionRepository.save(tipoCamion);
    }

    public ArrayList<TipoCamion> listarTiposCamion() {
        return (ArrayList<TipoCamion>) tipoCamionRepository.findAll();
    }

    public TipoCamion obtenerDatosTipoCamion (int id){ return (TipoCamion) tipoCamionRepository.obtenerDatosTipoCamion(id);}



}