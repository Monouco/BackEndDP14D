package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.*;
import com.grupo4D.sag_system.model.request.AveriaFront;
import com.grupo4D.sag_system.repository.RutaRepository;
import com.grupo4D.sag_system.repository.RutaXNodoRepository;
import com.grupo4D.sag_system.service.AveriaService;
import com.grupo4D.sag_system.service.CamionService;
import com.grupo4D.sag_system.service.MantenimientoService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private RutaXNodoRepository rutaXNodoRepository;

    @PostMapping("/guardarAveria")
    public Averia guardarAveria(@RequestBody Averia averiaModel){
        return averiaService.guardarAveria(averiaModel);
    }

    @PostMapping("/registrarAveriaNueva")
    public Averia registrarAveriaNueva(@RequestBody AveriaFront averia){
        long nanos = 1000000000;
        Camion camion = new Camion();
        Averia averiaModel = new Averia();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(averia.getFecha(), formatter);
        averiaModel.setActivo(true);
        camion.setId(averia.getIdCamion());
        averiaModel.setCamion(camion);
        averiaModel.setFechaIncidente(date);

        camionService.cambiarEstadoAveria("Averiado",averia.getIdCamion());

        Mantenimiento m = new Mantenimiento();
        m.setActivo(true);
        m.setCamion(camion);
        m.setFechaEntrada(date.plusHours(1));
        m.setFechaSalida(date.plusHours(49));
        m.setTipo("Correctivo");

        //long wholeRouteTime = (long)((tiempoAtencion/velocidad*atendidos + (j*1000)/velocity)*nanos);

        //Por ahora para día a día
        ArrayList<Ruta> rutasSolucion = rutaRepository.listarRutasDisponibles("Iniciado", 1); //sacar todas las rutas con estado iniciado
        if (!rutasSolucion.isEmpty()) {
            Ruta rutaCamion = new Ruta();
            for (Ruta r : rutasSolucion) {
                if (r.getCamion().getId() == averia.getIdCamion()) { //buscar el id de camion que corresponda
                    rutaCamion = r;
                    break;
                }
            }
            //restar fecha fin de fecha inicio
            LocalDateTime f1 = rutaCamion.getFechaInicio();
            LocalDateTime f2 = rutaCamion.getFechaFin();
            long tiempo = ChronoUnit.NANOS.between(rutaCamion.getFechaInicio(), rutaCamion.getFechaFin());

            //tiempo hasta la averia en nanos
            long tiempoAveria = ChronoUnit.NANOS.between(rutaCamion.getFechaInicio(), averiaModel.getFechaIncidente());

            //sacar toda la ruta de ese camion
            ArrayList<RutaXNodo> rutaXNodos = rutaXNodoRepository.listarRutaXNodosPorRuta(rutaCamion.getId());

            //contar la cantidad de entregas en la ruta
            int nEntregas = 0;
            for (RutaXNodo rn : rutaXNodos) {
                if (rn.getPedido() >= 0) {
                    nEntregas++;
                }
            }
            //con la velocidad y el tiempo sacar el nodo en el que estaba el camion
            long tiempoSinEntregas = tiempo - nEntregas * 600 * nanos;
            double km = (rutaXNodos.size() - 1) * 1000;
            double velocidad = km / tiempoSinEntregas; //velocidad en km/nanoseg
            double tiempo1Km = (1 / velocidad);

            //buscar el aproximado de la ubicacion por el tiempo
            double tiempoAproximado = 0;
            int i;
            for (i = 0; i < rutaXNodos.size(); i++) {
                tiempoAproximado += tiempo1Km;
                if (tiempoAproximado > tiempoAveria)
                    break;
                if (rutaXNodos.get(i).getPedido() >= 0) {
                    tiempoAproximado += 600 * nanos;
                }
                if (tiempoAproximado > tiempoAveria)
                    break;
            }

            //registrar el x y y en averia
            averiaModel.setUbicacion(rutaXNodos.get(i).getNodo());
        }

        Mantenimiento mRespuesta = mantenimientoService.guardarMantenimientoNuevo(m);
        averiaModel.setMantenimiento(mRespuesta);
        return averiaService.guardarAveriaNueva(averiaModel);
    }

    @GetMapping("/listarAverias")
    public List<Averia> listarAverias(){
        return averiaService.listarAverias();
    }

    @GetMapping("/listarRutaPorNodosPorIDRuta")
    public ArrayList<RutaXNodo>listarRutaPorNodosPorIDRuta(){
        ArrayList<RutaXNodo> rutaXNodos = rutaXNodoRepository.listarRutaXNodosPorRuta(1);
        return rutaXNodos;
    }


}
