package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Averia;
import com.grupo4D.sag_system.model.Camion;
import com.grupo4D.sag_system.model.Mantenimiento;
import com.grupo4D.sag_system.model.request.AveriaFront;
import com.grupo4D.sag_system.service.AveriaService;
import com.grupo4D.sag_system.service.CamionService;
import com.grupo4D.sag_system.service.MantenimientoService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/averia")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class AveriaController {
    @Autowired
    private AveriaService averiaService;

    @Autowired
    private CamionService camionService;

    @Autowired
    private MantenimientoService mantenimientoService;

    @PostMapping("/guardarAveria")
    public Averia guardarAveria(@RequestBody Averia averiaModel){
        return averiaService.guardarAveria(averiaModel);
    }

    @PostMapping("/registrarAveriaNueva")
    public Averia registrarAveriaNueva(@RequestBody AveriaFront averia){
        Camion camion = new Camion();
        Averia averiaModel = new Averia();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(averia.getFecha(), formatter);
        averiaModel.setActivo(true);
        camion.setId(averia.getIdCamion());
        averiaModel.setCamion(camion);
        averiaModel.setFechaIncidente(date);

        camionService.cambiarEstadoAveria("AVERIADO",averia.getIdCamion());

        Mantenimiento m = new Mantenimiento();
        m.setActivo(true);
        m.setCamion(camion);
        m.setFechaEntrada(date.plusHours(1));
        m.setFechaSalida(date.plusHours(49));
        m.setTipo("CORRECTIVO");

        Mantenimiento mRespuesta = mantenimientoService.guardarMantenimientoNuevo(m);
        averiaModel.setMantenimiento(mRespuesta);
        return averiaService.guardarAveriaNueva(averiaModel);
    }

    @GetMapping("/listarAverias")
    public List<Averia> listarAverias(){
        return averiaService.listarAverias();
    }
}
