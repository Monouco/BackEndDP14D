package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.request.Fecha1TipoFront;
import com.grupo4D.sag_system.model.request.Fecha2TipoFront;
import com.grupo4D.sag_system.model.request.FechaFront;
import com.grupo4D.sag_system.model.response.ConsumoPetroleoFront;
import com.grupo4D.sag_system.model.response.RepGLPEntregadoXCamionFront;
import com.grupo4D.sag_system.model.reports.ReporteCamionConsumoMensual;

import com.grupo4D.sag_system.service.ReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ArrayList<RepGLPEntregadoXCamionFront> reporteGLPporDia(@RequestBody Fecha1TipoFront req){
        return reportesService.glpXCamionXFecha(req);
    }

    @PostMapping("/consumoPetroleoXFechas")
    public ConsumoPetroleoFront reporteConsumoPetroleoXFechas(@RequestBody Fecha2TipoFront req){
        return reportesService.consumoPetroleoDiario(req);
    }

    @PostMapping("/ConsumoMensual")
    public ResponseEntity<Resource> reporteConsumoMensual(@RequestBody FechaFront fecha) throws Exception{
        String filename = "ReporteConsumoMensual.xlsx";
        InputStreamResource file = reportesService.reporteConsumoMensual(fecha.getFechaIni(), fecha.getFechaFin());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
