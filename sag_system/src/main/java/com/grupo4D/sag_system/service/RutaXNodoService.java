package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.RutaXNodo;
import com.grupo4D.sag_system.repository.RutaXNodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RutaXNodoService{
    @Autowired
    RutaXNodoRepository rutaXNodoRepository;

    public RutaXNodo guardarRutaXNodo(RutaXNodo rutaXNodo){
        return rutaXNodoRepository.save(rutaXNodo);
    }

    public RutaXNodo guardarRutaXNodoNuevo(RutaXNodo rutaXNodo){
        return rutaXNodoRepository.save(rutaXNodo);
    }

    public ArrayList<RutaXNodo> listarRutasXNodo() {
        return (ArrayList<RutaXNodo>) rutaXNodoRepository.findAll();
    }

}

