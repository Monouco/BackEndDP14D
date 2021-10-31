package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.SagSystemApplication;
import com.grupo4D.sag_system.model.*;
import com.grupo4D.sag_system.model.algorithm.Ant;
import com.grupo4D.sag_system.model.algorithm.DepositGLP;
import com.grupo4D.sag_system.model.algorithm.Mapa;
import com.grupo4D.sag_system.model.algorithm.Order;
import com.grupo4D.sag_system.model.algorithm.pathFinding.ACSAlgorithm;
import com.grupo4D.sag_system.model.response.*;
import com.grupo4D.sag_system.model.runnable.AlgorithmThread;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class AlgorithmService {
    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    CamionRepository camionRepository;

    @Autowired
    RutaRepository rutaRepository;

    @Autowired
    NodoRepository nodoRepository;

    @Autowired
    RutaXNodoRepository rutaXNodoRepository;

    @Autowired
    RutaXPedidoRepository rutaXPedidoRepository;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext applicationContext;


    public RespuestaObtenerRutaFront obtenerRutasSolucion(Fecha fecha, double velocidad, int tipo){
        //parametros varios
        int tiempoAtencion =  600;
        double velocity = (double)500/36*velocidad; //a metros por segundo 500/36*velocidad
        //velocidad maxima = 100

        //multiplicador de segundos a nano
        long nanos = 1000000000;
        long desfase;
        int bandera = 0;
        RespuestaObtenerRutaFront apiResponse = new RespuestaObtenerRutaFront();


        //arreglo para pasar a front
        ArrayList<RespuestaRutaFront> respuesta = new ArrayList<>();

        //arreglo con la solucion de toda la flota
        ArrayList<RutaFront> solucion = new ArrayList<>();
        //solucion = this.asignarPedidos(fecha);
        //Obtendremos todas las rutas iniciadas
        ArrayList<Ruta> rutasSolucion = rutaRepository.listarRutasDisponibles("Iniciado", tipo);
        if (!rutasSolucion.isEmpty()){
            desfase = rutasSolucion.get(0).getDesfase();
            int i,j, atendidos;
            for(i = 0; i < rutasSolucion.size(); i++){
                ArrayList<NodoFront> nodos = new ArrayList<>();
                Ruta ruta = rutasSolucion.get(i);
                RespuestaRutaFront nodoRRF = new RespuestaRutaFront();
                nodoRRF.setStartDate(ruta.getFechaInicio());
                nodoRRF.setEndDate(ruta.getFechaFin()); // TODO: corregir el endDate
                nodoRRF.setAttentionTime((int)(tiempoAtencion/velocidad));
                nodoRRF.setVelocity(velocity);

                /*ArrayList<RutaXNodo> nodoRutas = rutaXNodoRepository.findRutaXNodosByIdAndActivoTrueOrderBySecuenciaAsc(
                        rutasSolucion.get(i).getId()
                );*/

                ArrayList<RutaXNodo> nodoRutas = rutaXNodoRepository.listarRutaXNodosPorRuta(
                        rutasSolucion.get(i).getId()
                );

                if (nodoRutas.size() <= 2) continue;

                atendidos = 0;
                j = 0;
                for(RutaXNodo nodo: nodoRutas){
                    Nodo nodoCoor = nodoRepository.findNodoById(nodo.getNodo().getId());
                    //nodos.add(new int [] {nodoCoor.getCoordenadaX(),nodoCoor.getCoordenadaY(),nodo.getPedido()});

                    nodos.add(new NodoFront(nodoCoor.getCoordenadaX(),nodoCoor.getCoordenadaY(),nodo.getPedido()));

                    j = nodo.getSecuencia();

                    if(nodo.getPedido()>=0){
                        RespuestaNodoFront orderRNF = new RespuestaNodoFront();
                        orderRNF.setIndexRoute(j);
                        long startAttention = (long)((tiempoAtencion/velocidad*atendidos + (j*1000)/velocity)*nanos + desfase);
                        long endAttention = (long) ((tiempoAtencion/velocidad*(atendidos+1) + (j*1000)/velocity)*nanos + desfase);
                        orderRNF.setDeliveryDate(ruta.getFechaInicio().plusNanos(startAttention));
                        orderRNF.setLeftDate(ruta.getFechaInicio().plusNanos(endAttention));
                        nodoRRF.getOrders().add(orderRNF);
                        atendidos ++;
                    }

                }

                nodoRRF.setRoute(nodos);

                long wholeRouteTime = (long)((tiempoAtencion/velocidad*atendidos + (j*1000)/velocity)*nanos + desfase);

                nodoRRF.setEndDate(nodoRRF.getStartDate().plusNanos(wholeRouteTime));

                //if(nodoRRF.getOrders().size()>0){
                    respuesta.add(nodoRRF);
                //}
                //rutasSolucion.get(i).set
            }
        }

        switch (tipo){
            case 1: {
                if(StaticValues.collapseFlag){
                    bandera = 2;
                }
                break;
            }
            case 2: {
                if(StaticValues.endSimulationFlag){
                    bandera = 1;
                }
                if(StaticValues.collapseSimulationFlag){
                    bandera = 2;
                }
                break;
            }
            case 3: {
                if(StaticValues.fullCollapseFlag){
                    bandera = 2;
                }
                break;
            }
        }

        apiResponse.setFlag(bandera);
        apiResponse.setRoutes(respuesta);

    /*
        for (RutaFront ruta:solucion) {
            RespuestaRutaFront nodoRRF = new RespuestaRutaFront();
            nodoRRF.setStartDate(ruta.getStartDate());
            nodoRRF.setEndDate(ruta.getStartDate()); // TODO: corregir el endDate
            nodoRRF.setTimeAttention((int)(tiempoAtencion/velocidad));
            nodoRRF.setVelocity(velocity);
            //nodoRRF.setRoute(ruta);
            ArrayList<NodoFront> nodos = ruta.getPath();

            for ( i=0; i< nodos.size();i++){
                RespuestaNodoFront orderRNF = new RespuestaNodoFront();
                if(nodos.get(i).getPedido()>=0){
                    orderRNF.setX(nodos.get(i).getX());
                    orderRNF.setY(nodos.get(i).getY());
                    orderRNF.setIndexRoute(i);
                    orderRNF.setDeliveryDate(ruta.getStartDate());
                    orderRNF.setLeftDate(ruta.getStartDate().plusSeconds(nodoRRF.getTimeAttention()));
                    nodoRRF.getOrders().add(orderRNF);
                }
            }
//            if(nodoRRF.getOrders().size()==0){
//                RespuestaNodoFront orderRNF = new RespuestaNodoFront();
//                nodoRRF.getOrders().add(orderRNF);
//            }
            if(nodoRRF.getOrders().size()>0){
                respuesta.add(nodoRRF);
            }
        }*/
        return apiResponse;
    }


    //public ArrayList<RutaFront> asignarPedidos(LocalDateTime horaInicio){
    //public ArrayList<RutaFront> asignarPedidos(String fecha){
    public ArrayList<RutaFront> asignarPedidos(LocalDateTime fecha, ArrayList<Pedido> pedidosNuevos, int tipo, long desfase){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
        //System.out.print("INICIO\n" +fecha+"\nFECHA RECIBIDA\n");
        //String[] fechas = fecha.split(":");
        //System.out.print("INICIO\n" +fechas[0]+"\nFECHA RECIBIDA\n");
        //System.out.print("INICIO\n" +fechas[1]+"\nFECHA RECIBIDA\n");
        //LocalDateTime horaInicio = LocalDateTime.parse(fecha, formatter);
        //LocalDateTime horaInicio = fecha.getFecha();
        LocalDateTime horaInicio = fecha;

        ArrayList<RutaFront> solucion = new ArrayList<>();

        //ArrayList<Pedido> pedidosNuevos = new ArrayList<>();
        //pedidosNuevos = pedidoRepository.findPedidosByEstadoPedido("Nuevo");

        if (pedidosNuevos.isEmpty()){
            return null;
        }

        //ver camiones disponibles
        ArrayList<Camion> camionesDisponibles = new ArrayList<>();
        camionesDisponibles = camionRepository.findCamionsByEstadoAndActivoTrue("Operativo", tipo); //Cambiar estado de double a string en BD y el resto

        //plantas
        Mapa mapa1 = new Mapa(50, 70);
        DepositGLP principal = new DepositGLP(12, 8, 100);
        DepositGLP almacenNorte = new DepositGLP(42, 42, 160);
        DepositGLP alamacenEste = new DepositGLP(63, 3, 160);

        mapa1.addDeposit(principal);
        mapa1.addDeposit(almacenNorte);
        mapa1.addDeposit(alamacenEste);

        //Nodos
//        for(int i = 0; i < 70; i++){
//            for(int j = 0; j < 50; j++){
//                Nodo nodo = new Nodo(i,j);
//                nodoRepository.save(nodo);
//            }
//        }

        //revisar
        int numAlmacenes = 3;
        int numOrdenes = pedidosNuevos.size();
        int hTurno = 24;
        int cycles = 200;
        int steps = 100;
        int tiempoAtencion = 600;
        long nanos = 1000000000;
        double velocity = (double)10/36;
        double evaporationRate = 0.3;
        //revisar


        //camiones a hormigas
        ArrayList<Ant> hormigas = new ArrayList<>();
        for (int i =0; i< camionesDisponibles.size();i++){
            Ant hormiga = new Ant(camionesDisponibles.get(i).getTipoCamion().getCapacidadGLP(),
                    camionesDisponibles.get(i).getTipoCamion().getCapacidadPetroleo(),mapa1.getPlantaPrincipal()[0],
                    mapa1.getPlantaPrincipal()[1],camionesDisponibles.get(i).getVelocidad(),
                    camionesDisponibles.get(i).getTipoCamion().getPesoTara(),
                    camionesDisponibles.get(i).getTipoCamion().getPesoGLP());
            hormigas.add(hormiga);
        }


        //pedidos a ordenes
        ArrayList<Order> ordenes = new ArrayList<>();
        for (int i =0; i< pedidosNuevos.size();i++){
            Order orden = new Order(pedidosNuevos.get(i).getFechaEntrega().getDayOfMonth(),
                    pedidosNuevos.get(i).getFechaEntrega().getHour(),
                    pedidosNuevos.get(i).getFechaEntrega().getMinute(),
                    pedidosNuevos.get(i).getNodo().getCoordenadaX(),
                    pedidosNuevos.get(i).getNodo().getCoordenadaY(),
                    pedidosNuevos.get(i).getCantidadGLP(),
                    pedidosNuevos.get(i).getPlazoEntrega(),
                    pedidosNuevos.get(i).getFechaPedido(),
                    pedidosNuevos.get(i).getFechaPedido().plusHours(
                            pedidosNuevos.get(i).getPlazoEntrega()
                    ));
            ordenes.add(orden);
            //Actualizando estado
            pedidosNuevos.get(i).setEstadoPedido("En ruta");
            //pedidoRepository.save(pedidosNuevos.get(i));
        }
        pedidoRepository.saveAll(pedidosNuevos);

        ACSAlgorithm algoritmoACS  = new ACSAlgorithm(numAlmacenes, numOrdenes,mapa1.getPlantaPrincipal(),hTurno);

        hormigas = algoritmoACS.findSolution(hormigas,ordenes,mapa1,cycles,steps, camionesDisponibles.size(),evaporationRate);

        //hormigas a camiones
        ArrayList<RutaXNodo> secuenciaRuta = new ArrayList<>();
        ArrayList<RutaXPedido> secuenciaPedido = new ArrayList<>();
        for (int i =0; i< camionesDisponibles.size();i++){

            if(hormigas.get(i).getBestRoute().size() <= 2) continue;

            Ruta ruta = new Ruta();
            ruta.setCamion(camionesDisponibles.get(i));

            ruta.setFechaInicio(horaInicio.plusSeconds(10));
            ruta.setEstado("Iniciado");
            ruta.setTipo(tipo);
            ruta.setDesfase(desfase);
            //ruta.setFechaInicio(horaInicio.plusSeconds(20)+);

            rutaRepository.save(ruta);

            ArrayList<NodoFront> path = new ArrayList<>();
            int k = 0, atendidos = 0;
            for (int[]j:hormigas.get(i).getBestRoute()) {
                Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(j[0],j[1]);
                RutaXNodo rutaXNodo = new RutaXNodo();
                rutaXNodo.setNodo(nodo);
                rutaXNodo.setRuta(ruta);
                rutaXNodo.setSecuencia(k);
                //rutaXNodoRepository.save(rutaXNodo);

                //respuesta para el front
                NodoFront nodoFront = new NodoFront();
                nodoFront.setX(j[0]);
                nodoFront.setY(j[1]);
                if(j.length ==3){
                    nodoFront.setPedido(j[2]);
                    rutaXNodo.setPedido(j[2]);
                    if(j[2] >= 0){
                        atendidos++;
                    }
                }else{
                    rutaXNodo.setPedido(-1);
                    nodoFront.setPedido(-1);
                }
                path.add(nodoFront);
                secuenciaRuta.add(rutaXNodo);
                k++;
            }
            long tiempoRuta = (long)(( (velocity * hormigas.get(i).getVelocity())/(1000 * k) + atendidos * tiempoAtencion) * nanos);
            ruta.setFechaFin(ruta.getFechaInicio().plusNanos(tiempoRuta));
            rutaRepository.save(ruta);

            RutaFront rutaFront = new RutaFront(ruta.getId(), ruta.getFechaInicio(), path);
            for (int j: hormigas.get(i).getBestSolution()) {
                if (j>=0){
                    RutaXPedido rutaXPedido = new RutaXPedido();
                    rutaXPedido.setPedido(pedidosNuevos.get(j));
                    rutaXPedido.setRuta(ruta);
                    //rutaXPedidoRepository.save(rutaXPedido);
                    secuenciaPedido.add(rutaXPedido);
                }
            }
            rutaFront.setPath(path);
            solucion.add(rutaFront);

            switch (tipo){
                case 1: {
                    camionesDisponibles.get(i).setEstado("En Ruta");
                    break;
                }
                case 2:{
                    camionesDisponibles.get(i).setEstadoSimulacion("En Ruta");
                    break;
                }
                case 3:{
                    camionesDisponibles.get(i).setEstadoColapso("En Ruta");
                    break;
                }
            }

        }
        rutaXNodoRepository.saveAll(secuenciaRuta);
        rutaXPedidoRepository.saveAll(secuenciaPedido);
        return solucion;
    }

    public ArrayList<Pedido> asignarPedidos3Dias(/*Fecha fecha, */ArrayList<Pedido> pedidos){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
        LocalDateTime fechaInicio = pedidos.get(0).getFechaPedido(), fechaTemp;
        LocalDateTime fechaFin = fechaInicio;
        int i;
        for(i = 0; i<pedidos.size();i++){
            pedidos.get(i).setEstadoPedido("Nuevo");
            Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(pedidos.get(i).getNodo().getCoordenadaX(),
                    pedidos.get(i).getNodo().getCoordenadaY());
            pedidos.get(i).getNodo().setId(nodo.getId());
            pedidos.get(i).setTipo(2);  //2 es simulacion 3 dias
            fechaTemp = pedidos.get(i).getFechaPedido();
            if(fechaTemp.isBefore(fechaInicio))
                fechaInicio = fechaTemp;
            if(fechaTemp.isAfter(fechaFin))
                fechaFin = fechaTemp;
        }
        pedidoRepository.saveAll(pedidos);

        //ArrayList<Pedido> pedidosActuales = pedidoRepository.listarPedidosDisponibles(fechaInicio, 2);

        //Creamos un thread para el algoritmo
        StaticValues.mult = 14;
        StaticValues.start = LocalDateTime.now();
        StaticValues.virtualDate = fechaInicio.plusMinutes(15);
        StaticValues.simulationType = 2;
        StaticValues.end = fechaFin;

        AlgorithmThread algorithm = applicationContext.getBean(AlgorithmThread.class);

        taskExecutor.execute(algorithm);



        return pedidos;

    }


}
