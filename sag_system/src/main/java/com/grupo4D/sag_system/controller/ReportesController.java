package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Fecha;
import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.reports.ReporteCapacidadAtencion;
import com.grupo4D.sag_system.model.request.Fecha1TipoFront;
import com.grupo4D.sag_system.model.request.Fecha2TipoFront;
import com.grupo4D.sag_system.model.request.FechaFront;
import com.grupo4D.sag_system.model.response.ConsumoPetroleoFront;
import com.grupo4D.sag_system.model.response.RepGLPEntregadoXCamionFront;
import com.grupo4D.sag_system.model.reports.ReporteCamionConsumoMensual;

import com.grupo4D.sag_system.model.statics.StaticValues;
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
        InputStreamResource file = reportesService.reporteConsumoMensual(fecha.getFechaIni(), fecha.getFechaFin(), fecha.getTipo());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @PostMapping("/capacidadAtencionMensual")
    public ResponseEntity<Resource> reporteCapacidadAtencion(@RequestBody Fecha fecha) throws Exception{
        String filename = "ReporteCapacidadAtencionMensual.xlsx";
        InputStreamResource file = reportesService.reporteCapacidadAtencion(fecha.getTipo());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @PostMapping("/capacidadAtencion")
    public ReporteCapacidadAtencion capacidadAtencion(@RequestBody Fecha fecha) throws Exception{
        return reportesService.capacidadAtencion(fecha.getTipo());
    }

    @PostMapping("/capacidadAtencionMensualColapso")
    public ResponseEntity<Resource> reporteCapacidadAtencionColapso(@RequestBody Fecha fecha) throws Exception{
        String filename = "ReporteCapacidadAtencionMensual.xlsx";
        InputStreamResource file;
        switch (fecha.getTipo()){
            case(1) : {
                file = StaticValues.reportCapacity;
                StaticValues.reportCapacity = null;
                break;
            }
            case(2) : {
                file = StaticValues.simReportCapacity;
                StaticValues.simReportCapacity = null;
                break;
            }
            case(3) : {
                file = StaticValues.collapseReportCapacity;
                StaticValues.collapseReportCapacity = null;
                break;
            }
            default:{
                file = null;
                break;
            }
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
