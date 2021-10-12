package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.Ruta;
import com.grupo4D.sag_system.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RutaService{
    @Autowired
    RutaRepository rutaRepository;

    public Ruta guardarRuta(Ruta ruta){
        return rutaRepository.save(ruta);
    }

    public Ruta guardarRutaNueva(Ruta ruta){
        return rutaRepository.save(ruta);
    }

    public ArrayList<Ruta> listarRutas() {
        return (ArrayList<Ruta>) rutaRepository.findAll();
    }

}

