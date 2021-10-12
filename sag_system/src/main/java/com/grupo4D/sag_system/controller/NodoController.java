package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.service.NodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nodo")
//@CrossOrigin("*")
public class NodoController {
    @Autowired
    private NodoService nodoService;

    @PostMapping("/guardarNodo")
    public Nodo guardarNodo(@RequestBody Nodo nodoModel){
        return nodoService.guardarNodo(nodoModel);
    }

    @PostMapping("/registrarNodoNuevo")
    public Nodo registrarNodoNuevo(@RequestBody Nodo nodoModel){
        return nodoService.guardarNodoNuevo(nodoModel);
    }

    @GetMapping("/listarNodos")
    public List<Nodo> listarNodos(){
        return nodoService.listarNodos();
    }
}

