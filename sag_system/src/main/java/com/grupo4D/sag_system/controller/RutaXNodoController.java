package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.RutaXNodo;
import com.grupo4D.sag_system.service.RutaXNodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rutaXNodo")
//@CrossOrigin("*")
public class RutaXNodoController {
    @Autowired
    private RutaXNodoService rutaXNodoService;

    @PostMapping("/guardarRutaXNodo")
    public RutaXNodo guardarRutaXNodo(@RequestBody RutaXNodo rutaXNodoModel){
        return rutaXNodoService.guardarRutaXNodo(rutaXNodoModel);
    }

    @PostMapping("/registrarRutaXNodoNueva")
    public RutaXNodo registrarRutaXNodoNueva(@RequestBody RutaXNodo rutaXNodoModel){
        return rutaXNodoService.guardarRutaXNodoNuevo(rutaXNodoModel);
    }

    @GetMapping("/listarRutasXNodo")
    public List<RutaXNodo> listarRutasXNodo(){
        return rutaXNodoService.listarRutasXNodo();
    }
}


