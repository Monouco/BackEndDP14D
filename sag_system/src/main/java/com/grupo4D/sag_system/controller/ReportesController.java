package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.request.FechaFront;
import com.grupo4D.sag_system.model.response.RepGLPEntregadoXCamionFront;

import com.grupo4D.sag_system.service.ReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/reportes")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class ReportesController {
    @Autowired
    private ReportesService reportesService;

    @PostMapping("/GLPEntregadoXCamion")
    public ArrayList<RepGLPEntregadoXCamionFront> reporteGLPporDia(@RequestBody FechaFront fecha){
        return reportesService.glpXCamionXFecha(fecha);
    }
}
