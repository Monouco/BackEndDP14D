package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.*;
import com.grupo4D.sag_system.model.request.AveriaFront;
import com.grupo4D.sag_system.model.runnable.AveriaThread;
import com.grupo4D.sag_system.model.runnable.UpdateCurrentValues;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.*;
import com.grupo4D.sag_system.service.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
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
    private CamionRepository camionRepository;

    @Autowired
    private MantenimientoService mantenimientoService;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private RutaXNodoRepository rutaXNodoRepository;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/guardarAveria")
    public Averia guardarAveria(@RequestBody Averia averiaModel){
        return averiaService.guardarAveria(averiaModel);
    }

    @PostMapping("/registrarAveriaNueva")
    public Averia registrarAveriaNueva(@RequestBody AveriaFront averia){
        long nanos = 1000000000;
        Camion camion = camionRepository.findCamionById(averia.getIdCamion());
        Averia averiaModel = new Averia();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(averia.getFecha(), formatter);
        averiaModel.setActivo(true);
        //camion.setId(averia.getIdCamion());
        averiaModel.setCamion(camion);
        averiaModel.setFechaIncidente(date);
        averiaModel.setTipo(averia.getType());

        //Calculamos el desfase
        long desfase = ChronoUnit.NANOS.between(LocalDateTime.now(StaticValues.zoneId),date);
        averiaModel.setDesfase(desfase);

        //camionService.cambiarEstadoAveria("Averiado",averia.getIdCamion());
        switch(averia.getType()){
            case 1:{
                camion.setEstado("Averiado");
                break;
            }
            case 2:{
                camion.setEstadoSimulacion("Averiado");
                break;
            }
            case 3:{
                camion.setEstadoColapso("Averiado");
                break;
            }
        }
        camionRepository.save(camion);

        Mantenimiento m = new Mantenimiento();
        m.setActivo(true);
        m.setCamion(camion);
        m.setFechaEntrada(date.plusHours(1));
        m.setFechaSalida(date.plusHours(49));
        m.setTipo("Correctivo");
        m.setTipoSimulacion(averia.getType());

        //long wholeRouteTime = (long)((tiempoAtencion/velocidad*atendidos + (j*1000)/velocity)*nanos);

        //Por ahora para día a día
        ArrayList<Ruta> rutasSolucion = rutaRepository.listarRutasDisponibles("Iniciado", averia.getType()); //sacar todas las rutas con estado iniciado
        System.out.println("Acaba de obtener las rutas Iniciadas" );
        if (!rutasSolucion.isEmpty()) {
            Ruta rutaCamion = new Ruta();
            for (Ruta r : rutasSolucion) {
                if (r.getCamion().getId() == averia.getIdCamion()) { //buscar el id de camion que corresponda
                    rutaCamion = r;
                    break;
                }
            }
            if (rutaCamion.getId()!=0) {
                System.out.println("Hizo match ruta con id camion" );

                //Cambiamos el valor de la rutaCamion a cancelada o averiada
                rutaCamion.setEstado("Averiada");
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
                System.out.println("Calculo de tiempo sin entregas" );
                //con la velocidad y el tiempo sacar el nodo en el que estaba el camion
                long tiempoSinEntregas = tiempo - nEntregas * 600 * nanos; //TODO:cambiar el 600 por 600/velocidad esto en simulacion 3 dias
                double km = (rutaXNodos.size() - 1); //* 1000;
                double velocidad = km / tiempoSinEntregas; //velocidad en km/nanoseg
                double tiempo1Km = (1 / velocidad);

                //buscar el aproximado de la ubicacion por el tiempo
                double tiempoAproximado = 0;
                int i, index = rutaXNodos.size() - 1;
                boolean flag = false;
                for (i = 0; i < rutaXNodos.size(); i++) {
                    if(!flag) {
                        tiempoAproximado += tiempo1Km;
                        //Supongo que aca obtiene el valor, por lo que a partir de este nodo, vamos a volver el activo 0 para que no se tomen en cuenta
                        if (tiempoAproximado > tiempoAveria) {
                            index = i;
                            flag = true;
                        }
                        if (rutaXNodos.get(i).getPedido() >= 0) {
                            tiempoAproximado += 600 * nanos;
                        }
                        //Esto es que se averia atendiendo a alguien?
                        if (tiempoAproximado > tiempoAveria ) {
                            index = i;
                            flag = true;
                        }
                    }
                    else{
                        rutaXNodos.get(i).setActivo(false);
                        continue;
                    }

                }
                //registrar el x y y en averia
                System.out.println("Va a registrar el nodo en la averia" );
                averiaModel.setUbicacion(rutaXNodos.get(index).getNodo());
                if (averiaModel.getUbicacion()== null){
                    //si no se logro encontrar la averia se registra en 0,0
                    Nodo n0 = new Nodo(0,0);
                    n0.setId(1);
                    averiaModel.setUbicacion(n0);
                }
                System.out.println("Registro el nodo: x: "+ rutaXNodos.get(index).getNodo().getCoordenadaX()+" y: "+rutaXNodos.get(index).getNodo().getCoordenadaY());
                //Guardamos los valores cambiados de la ruta y nodos de la rutaXNodo
                rutaRepository.save(rutaCamion);
                rutaXNodoRepository.saveAll(rutaXNodos);
                //Ahora tenemos que averiar los nodosXAlmacen y nodosXPedidos a partir del nodo donde se quedo
                rutaRepository.devolverGLP(index, averia.getType(), rutaCamion.getId());
            }
        }

        Mantenimiento mRespuesta = mantenimientoService.guardarMantenimientoNuevo(m);
        averiaModel.setMantenimiento(mRespuesta);


        averiaModel.setDesfase(desfase);

        StaticValues.idCamion = averia.getIdCamion();
        //Aca se tiene que recibir el multiplicador y el tipo;
        StaticValues.mult = averia.getMultiplier(); //TODO: este campo hay que obtenerlo
        StaticValues.simulationType = averia.getType();

        AveriaThread updating = applicationContext.getBean(AveriaThread.class);

        System.out.println("######################################### Generando Averia");

        taskExecutor.execute(updating);

        //Ocurrian problemas debido a que el algoritmo en simulacion se ejecuta cada 3 segundos
        if(averia.getType() == 1){
            ArrayList<Pedido> pedidos = pedidoService.listarPedidosDisponibles(date, averia.getType());
            algorithmService.asignarPedidos(date,pedidos, averia.getType(), desfase, averia.getMultiplier());
        }

        System.out.println("Terminado de re programar los pedidos de la averia del camion " + averia.getIdCamion()+" del tipo " + averia.getType());

        if (averiaModel.getUbicacion()== null){
            //si no se logro encontrar la averia se registra en 0,0
            Nodo n0 = new Nodo(0,0);
            n0.setId(1);
            averiaModel.setUbicacion(n0);
        }

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

    @PostMapping("/obtenerAverias")
    public ArrayList<AveriaFront> obtenerAverias(@RequestBody Fecha obj){
        return averiaService.obtenerAverias(obj.getVelocidad(), obj.getTipo());
    }

}
