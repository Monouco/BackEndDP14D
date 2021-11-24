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
import com.grupo4D.sag_system.model.runnable.AveriaScheduled;
import com.grupo4D.sag_system.model.runnable.FillDeposit;
import com.grupo4D.sag_system.model.runnable.UpdateCurrentValues;
import com.grupo4D.sag_system.model.statics.ConcurrentValues;
import com.grupo4D.sag_system.model.statics.OutputLog;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    TipoCamionRepository tipoCamionRepository;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    BloqueoRepository bloqueoRepository;

    @Autowired
    NodoXBloqueoRepository nodoXBloqueoRepository;

    @Autowired
    RutaXPlantaRepository rutaXPlantaRepository;

    @Autowired
    PlantaRepository plantaRepository;

    public ArrayList<CamionHRFront> obtenerHojaDeRuta(TipoSimulacionFront t){
        ArrayList<CamionHRFront> hojaDeRuta = new ArrayList<>();
        //Se busca los camiones en ruta
        ArrayList<Camion> camionesEnRuta = camionRepository.listarCamionesTipo("En Ruta",t.getTipo());
//        for (int l=0;l<camionesEnRuta.size();l++){
//            System.out.print(camionesEnRuta.get(l).getId()+"\n");
//        }
//        System.out.print("Camiones arriba"+camionesEnRuta.size()+" \n");

        //Se busca las rutas iniciadas de acuerdo al parametro
        ArrayList<Ruta> rutasIniciadas = rutaRepository.listarRutasDisponibles("Iniciado", t.getTipo());
        //System.out.print("Rutas "+rutasIniciadas.size()+" \n");

        for (Camion c: camionesEnRuta) {
            CamionHRFront camionHR = new CamionHRFront();
            camionHR.setId(c.getId());
            camionHR.setCodigoCamion(camionRepository.listarCodigo1Camion(c.getId()));
            int i = 0;
            for (i=0;i<rutasIniciadas.size();i++){
                if (rutasIniciadas.get(i).getCamion().getId() == c.getId()){
                    String hora = String.format("%02d", rutasIniciadas.get(i).getFechaInicio().getHour());
                    String minutos = String.format("%02d", rutasIniciadas.get(i).getFechaInicio().getMinute());
                    String segundos = String.format("%02d", rutasIniciadas.get(i).getFechaInicio().getSecond());
                    String horaSalida = hora + ":" + minutos + ":"+ segundos;
                    hora = String.format("%02d", rutasIniciadas.get(i).getFechaFin().getHour());
                    minutos = String.format("%02d", rutasIniciadas.get(i).getFechaFin().getMinute());
                    segundos = String.format("%02d",  rutasIniciadas.get(i).getFechaFin().getSecond());
                    String horaLlegada =  hora+ ":" + minutos + ":"+ segundos;
                    camionHR.setHoraSalida(horaSalida);
                    camionHR.setHoraLlegada(horaLlegada);
                    i++;
                    break;
                };
            }

            double cantGLPTransportado=0;

            if (i!=0){
                //ArrayList<RutaXPedido> pedidosDeRuta = rutaXPedidoRepository.findRutaXPedidosByIdRuta(rutasIniciadas.get(i).getId());
                for (int j=0;j<i;j++){
                    Ruta r = rutasIniciadas.get(j);
                    ArrayList<RutaXPedido> pedidosDeRuta = rutaXPedidoRepository.findRutaXPedidosByRuta(r.getId());
                    ArrayList<PedidoHRFront> pedidos = new ArrayList<>();
                    for (RutaXPedido rxp:   pedidosDeRuta ) {
                        Pedido pedido1ruta = pedidoRepository.findPedidoByIdAndActivoTrue(rxp.getPedido().getId());
                        //System.out.print("pedido de RutaXPedido "+rxp.getPedido().getId()+"\n");
                        PedidoHRFront pedidoHR = new PedidoHRFront();
                        pedidoHR.setIdPedido(pedido1ruta.getId());
                        pedidoHR.setCantidadGLP(rxp.getCantidadGLPEnviado());
                        UbicacionHRFront u = new UbicacionHRFront();
                        u.setX(pedido1ruta.getNodo().getCoordenadaX());
                        u.setY(pedido1ruta.getNodo().getCoordenadaY());
                        pedidoHR.setUbicacion(u);
                        String hora = String.format("%02d", rxp.getFechaEntrega().getHour());
                        String minutos = String.format("%02d", rxp.getFechaEntrega().getMinute());
                        String segundos = String.format("%02d", rxp.getFechaEntrega().getSecond());
                        pedidoHR.setHoraLlegada( hora+":"+minutos+ ":"+ segundos);
                        LocalDateTime finAtencion = rxp.getFechaEntrega().plusMinutes(10);
                        hora = String.format("%02d", finAtencion.getHour());
                        minutos = String.format("%02d", finAtencion.getMinute());
                        segundos = String.format("%02d", finAtencion.getSecond());
                        pedidoHR.setHoraDeFinAtencion(hora+ ":"+ minutos+":"+ segundos);
                        //System.out.print("pedido "+pedido1ruta.getId()+"\n");
                        pedidos.add(pedidoHR);
                        cantGLPTransportado += rxp.getCantidadGLPEnviado();
                    }
                    camionHR.setNumPedidos(pedidos.size());
                    camionHR.setPedidos(pedidos);
                    camionHR.setCantGlpActual(cantGLPTransportado);
                    camionHR.setCantPetroleoActual(1.02);


                    //Se saca los nodos de una ruta en particular
                    ArrayList<RutaXNodo> nodosDeRuta = rutaXNodoRepository.listarRutaXNodosPorRuta(r.getId());

                    double glp=0;
                    double petroleo =0;
                    double distancia = 0;
                    double [] pesos = new double[nodosDeRuta.size()];
                    int salto=0;
                    pesos[0] = pedidosDeRuta.get(0).getCantidadGLPEnviado();
                    ArrayList<Integer> distancias = new ArrayList<>();
                    TipoCamion tCamion = tipoCamionRepository.listarTipoCamion(c.getId());
                    //Calculo de peso en el camion
                    for(int k=0;k<nodosDeRuta.size();k++){



                        double peso = 0;
                        //hallar el petroleo aca
                        //0 a mas es entrega de pedido
                        //-4,-3 es planta principal
                        //distancia x peso/150
//                        if(nodosDeRuta.get(k).getPedido()>=0){
//                            salto=salto++;
//                            pesos[j] = pedidosDeRuta.get(salto).getCantidadGLPEnviado();
//                        }else{
//
//                        }
                        System.out.println("Nodos de ruta: "+nodosDeRuta.get(k).getPedido());
                    }


                    //TODO: cantidadGLPActual y cantidadPetroleoActual en camionHR
                    //puede ser en rutaxnodo con dos columnas mas para cada valor

                }

            }

            hojaDeRuta.add(camionHR);
        }

        return hojaDeRuta;
    }




    public RespuestaObtenerRutaFront obtenerRutasSolucion(Fecha fecha, double velocidad, int tipo){
        //parametros varios
        int tiempoAtencion =  600;
        double velocity = (double)500/36*velocidad; //a metros por segundo 500/36*velocidad
        //velocidad maxima = 100

        //multiplicador de segundos a nano
        long nanos = 1000000000;
        long desfase;
        int bandera = 0, size = camionRepository.tamanoFlota(), curCount = 0;
        RespuestaObtenerRutaFront apiResponse = new RespuestaObtenerRutaFront();
        ArrayList<Integer> camiones = camionRepository.obtenerIds();
        HashMap<Integer,Integer> camionesList = new HashMap<>();
        for (int val:
             camiones) {
            camionesList.put(val, 0);
        }

        //arreglo para pasar a front
        ArrayList<RespuestaRutaFront> respuesta = new ArrayList<>();
        ArrayList<RespuestaRutaFront> respuestaTemp = new ArrayList<>();

        //arreglo con la solucion de toda la flota
        ArrayList<RutaFront> solucion = new ArrayList<>();
        //solucion = this.asignarPedidos(fecha);
        //Obtendremos todas las rutas iniciadas
        ArrayList<Ruta> rutasSolucion = rutaRepository.listarRutasDisponibles("Iniciado", tipo);
        if (!rutasSolucion.isEmpty()){

            int i,j, atendidos;
            for(i = 0; i < rutasSolucion.size(); i++){
                curCount++;
                desfase = rutasSolucion.get(i).getDesfase();
                ArrayList<NodoFront> nodos = new ArrayList<>();
                Ruta ruta = rutasSolucion.get(i);
                RespuestaRutaFront nodoRRF = new RespuestaRutaFront();
                nodoRRF.setStartDate(ruta.getFechaInicio().minusNanos(desfase));
                nodoRRF.setEndDate(ruta.getFechaFin());
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
                    //Nodo nodoCoor = nodoRepository.findNodoById(nodo.getNodo().getId());
                    Nodo nodoCoor = nodo.getNodo();
                    //nodos.add(new int [] {nodoCoor.getCoordenadaX(),nodoCoor.getCoordenadaY(),nodo.getPedido()});

                    nodos.add(new NodoFront(nodoCoor.getCoordenadaX(),nodoCoor.getCoordenadaY(),nodo.getPedido()));

                    j = nodo.getSecuencia();

                    if(nodo.getPedido()>=0){
                        RespuestaNodoFront orderRNF = new RespuestaNodoFront();
                        orderRNF.setIndexRoute(j);
                        long startAttention = (long)((tiempoAtencion/velocidad*atendidos + (j*1000)/velocity)*nanos - desfase);
                        long endAttention = (long) ((tiempoAtencion/velocidad*(atendidos+1) + (j*1000)/velocity)*nanos - desfase);
                        orderRNF.setDeliveryDate(ruta.getFechaInicio().plusNanos(startAttention));
                        orderRNF.setLeftDate(ruta.getFechaInicio().plusNanos(endAttention));
                        nodoRRF.getOrders().add(orderRNF);
                        atendidos ++;
                    }

                }

                nodoRRF.setRoute(nodos);

                long wholeRouteTime = (long)((tiempoAtencion/velocidad*atendidos + (j*1000)/velocity)*nanos);

                nodoRRF.setEndDate(nodoRRF.getStartDate().plusNanos(wholeRouteTime));
                nodoRRF.setIdCamion(rutasSolucion.get(i).getCamion().getId());

                camionesList.put(nodoRRF.getIdCamion(),1);

                //if(nodoRRF.getOrders().size()>0){
                respuestaTemp.add(nodoRRF);
                //}
                //rutasSolucion.get(i).set
            }
        }

        /*for(int i =0; i< size;i++){
            RespuestaRutaFront fill = new RespuestaRutaFront();
            fill.setActive(0);
            respuesta.add(fill);
        }*/

        int i = 0;
        RespuestaRutaFront fill;
        for (Map.Entry<Integer, Integer> entry : camionesList.entrySet()) {
            int key = entry.getKey();
            int val = entry.getValue();
            if(val == 1){
                fill = respuestaTemp.get(i);
                i++;
            }else{
                fill = new RespuestaRutaFront();
                fill.setActive(0);
                fill.setIdCamion(key);
            }
            respuesta.add(fill);
        }
        apiResponse.setCollapseInfo(null);

        switch (tipo){
            case 1: {
                if(StaticValues.collapseFlag){
                    bandera = 2;
                    apiResponse.setCollapseInfo(StaticValues.collapseStatus);
                    StaticValues.collapseFlag = false;
                }
                break;
            }
            case 2: {
                if(StaticValues.endSimulationFlag){
                    bandera = 1;
                    StaticValues.endSimulationFlag = false;
                }
                if(StaticValues.collapseSimulationFlag){
                    bandera = 2;
                    apiResponse.setCollapseInfo(StaticValues.collapseSimStatus);
                    StaticValues.collapseSimulationFlag = false;
                }
                break;
            }
            case 3: {
                if(StaticValues.fullCollapseFlag){
                    bandera = 2;
                    apiResponse.setCollapseInfo(StaticValues.fullCollapseStatus);
                    StaticValues.fullCollapseFlag = false;
                }
                break;
            }
        }

        apiResponse.setFlag(bandera);
        apiResponse.setRoutes(respuesta);

        return apiResponse;
    }


    //public ArrayList<RutaFront> asignarPedidos(LocalDateTime horaInicio){
    //public ArrayList<RutaFront> asignarPedidos(String fecha){
    public ArrayList<RutaFront> asignarPedidos(LocalDateTime fecha, ArrayList<Pedido> pedidosNuevos, int tipo, long desfase, int multiplier){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
        LocalDateTime horaInicio = fecha;

        ArrayList<RutaFront> solucion = new ArrayList<>();

        if (pedidosNuevos.isEmpty()){
            return null;
        }

        //ver camiones disponibles
        ArrayList<Camion> camionesDisponibles = new ArrayList<>();
        camionesDisponibles = camionRepository.listarCamionesTipo("Operativo", tipo); //Cambiar estado de double a string en BD y el resto

        if(camionesDisponibles.isEmpty()) return null;

        //plantas
        Mapa mapa1 = new Mapa(50, 70, bloqueoRepository, nodoXBloqueoRepository);
        ArrayList<Planta> plantas = plantaRepository.listarPlantas();

        double glpDeposit = 0;


        for (Planta planta:
             plantas) {
            switch (tipo){
                case 1: {
                    glpDeposit = planta.getGlpDisponible();
                    break;
                }
                case 2: {
                    glpDeposit = planta.getGlpDisponibleSimulacion();
                    break;
                }
                case 3: {
                    glpDeposit = planta.getGlpDisponibleColapso();
                    break;
                }
            }
            DepositGLP deposit = new DepositGLP(planta.getNodo().getCoordenadaX(), planta.getNodo().getCoordenadaY(), glpDeposit);
            mapa1.addDeposit(deposit);
            int [] coor = {plantas.get(0).getNodo().getCoordenadaX(),plantas.get(0).getNodo().getCoordenadaY()};
            mapa1.setPlantaPrincipal(coor);
        }
        /*
        DepositGLP principal = new DepositGLP(12, 8, 100);
        DepositGLP almacenNorte = new DepositGLP(42, 42, 160);
        DepositGLP alamacenEste = new DepositGLP(63, 3, 160);


        mapa1.addDeposit(principal);
        mapa1.addDeposit(almacenNorte);
        mapa1.addDeposit(alamacenEste);*/
        mapa1.initializeCurrentRoadBlocks(fecha, fecha.plusHours(12), tipo);


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
        boolean averiado = false;
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
            Order orden = new Order(
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

        ACSAlgorithm algoritmoACS  = new ACSAlgorithm(numAlmacenes, numOrdenes,mapa1.getPlantaPrincipal(),hTurno, fecha);

        hormigas = algoritmoACS.findSolution(hormigas,ordenes,mapa1,cycles,steps, camionesDisponibles.size(),evaporationRate);

        //hormigas a camiones
        ArrayList<RutaXNodo> secuenciaRuta = new ArrayList<>();
        ArrayList<RutaXPedido> secuenciaPedido = new ArrayList<>();
        ArrayList<RutaXPlanta> secuenciaPlanta = new ArrayList<>();
        long spentTime;
        for (int i =0; i< camionesDisponibles.size();i++){

            if(hormigas.get(i).getBestRoute().size() <= 2) continue;

            Ruta ruta = new Ruta();
            ruta.setCamion(camionesDisponibles.get(i));

            ruta.setFechaInicio(horaInicio.plusSeconds(10));
            ruta.setEstado("Iniciado");
            ruta.setTipo(tipo);
            ruta.setDesfase(desfase);
            //ruta.setFechaInicio(horaInicio.plusSeconds(20)+);

            //Dependiendo del camion, generamos la averia si es que es simulacion de 3 dias
            switch (StaticValues.numCamion){
                case(2): {
                    StaticValues.simulationType = tipo;
                    StaticValues.idCamion = camionesDisponibles.get(i).getId();
                    StaticValues.virtualDate = fecha;
                    StaticValues.start = fecha.plusHours(2);
                    StaticValues.mult = multiplier;
                    averiado = true;
                    AveriaScheduled averia = applicationContext.getBean(AveriaScheduled.class);
                    taskExecutor.execute(averia);
                    break;
                }
                case(4): {
                    StaticValues.simulationType = tipo;
                    StaticValues.idCamion = camionesDisponibles.get(i).getId();
                    StaticValues.virtualDate = fecha;
                    StaticValues.start = fecha.plusHours(3);
                    StaticValues.mult = multiplier;
                    averiado = true;
                    AveriaScheduled averia = applicationContext.getBean(AveriaScheduled.class);
                    taskExecutor.execute(averia);
                    break;
                }
                default: {
                    break;
                }
            }
            StaticValues.numCamion++;

            //rutaRepository.save(ruta);

            ArrayList<NodoFront> path = new ArrayList<>();
            int k = 0, atendidos = 0,  temp = 0;
            ArrayList<Integer> hBestSolution = hormigas.get(i).getBestSolution();
            ArrayList<Double> hBestGLP = hormigas.get(i).getBestSolutionGLP();
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
                    spentTime = (long)(( (1000 * k)/(velocity * hormigas.get(i).getVelocity()) + atendidos * tiempoAtencion) * nanos);
                    if(j[2] != -4) {
                        if (hBestSolution.size() > temp && hBestSolution.get(temp) >= 0) {
                            RutaXPedido rutaXPedido = new RutaXPedido();
                            rutaXPedido.setPedido(pedidosNuevos.get(hBestSolution.get(temp)));
                            rutaXPedido.setRuta(ruta);
                            rutaXPedido.setCantidadGLPEnviado(hBestGLP.get(temp) - hBestGLP.get(temp + 1));
                            rutaXPedido.setSecuencia(k);
                            //rutaXPedidoRepository.save(rutaXPedido);

                            rutaXPedido.setFechaEntrega(ruta.getFechaInicio().plusNanos(spentTime));
                            secuenciaPedido.add(rutaXPedido);

                            pedidosNuevos.get(j[2]).setGlpProgramado(pedidosNuevos.get(j[2]).getGlpProgramado() + rutaXPedido.getCantidadGLPEnviado());
                        }
                        else{
                            //Guardamos el glp que se consigue
                            int numPlanta = j[2] + numAlmacenes;
                            RutaXPlanta rutaXPlanta = new RutaXPlanta();
                            rutaXPlanta.setRuta(ruta);
                            rutaXPlanta.setFechaLLegada(ruta.getFechaInicio().plusNanos(spentTime));
                            rutaXPlanta.setSecuencia(k);
                            rutaXPlanta.setCantidadGLPRespostado(hBestGLP.get(temp + 1) - hBestGLP.get(temp));
                            rutaXPlanta.setPlanta(plantas.get(numPlanta));

                            switch (tipo){
                                case 1: {
                                    plantas.get(numPlanta).setGlpDisponible(plantas.get(numPlanta).getGlpDisponible()
                                            - rutaXPlanta.getCantidadGLPRespostado());
                                    break;
                                }
                                case 2: {
                                    plantas.get(numPlanta).setGlpDisponibleSimulacion(plantas.get(numPlanta).getGlpDisponibleSimulacion()
                                            - rutaXPlanta.getCantidadGLPRespostado());
                                    break;
                                }
                                case 3: {
                                    plantas.get(numPlanta).setGlpDisponibleColapso(plantas.get(numPlanta).getGlpDisponibleColapso()
                                            - rutaXPlanta.getCantidadGLPRespostado());
                                    break;
                                }
                            }

                            secuenciaPlanta.add(rutaXPlanta);
                        }

                        temp++;
                    }
                }else{
                    rutaXNodo.setPedido(-1);
                    nodoFront.setPedido(-1);
                }
                path.add(nodoFront);
                secuenciaRuta.add(rutaXNodo);
                k++;
            }
            long tiempoRuta = (long)(( (1000 * k)/(velocity * hormigas.get(i).getVelocity()) + atendidos * tiempoAtencion) * nanos);
            ruta.setFechaFin(ruta.getFechaInicio().plusNanos(tiempoRuta));
            rutaRepository.save(ruta);

            RutaFront rutaFront = new RutaFront(ruta.getId(), ruta.getFechaInicio(), path);
            /*int temp ;
            ArrayList<Integer> hBestSolution = hormigas.get(i).getBestSolution();
            ArrayList<Double> hBestGLP = hormigas.get(i).getBestSolutionGLP();
            for ( temp = 0; temp < hBestSolution.size(); temp++
                    //int j: hormigas.get(i).getBestSolution()
            ) {
                if (hBestSolution.get(temp) >= 0){
                    RutaXPedido rutaXPedido = new RutaXPedido();
                    rutaXPedido.setPedido(pedidosNuevos.get(hBestSolution.get(temp)));
                    rutaXPedido.setRuta(ruta);
                    rutaXPedido.setCantidadGLPEnviado(hBestGLP.get(temp) - hBestGLP.get(temp + 1));
                    //rutaXPedidoRepository.save(rutaXPedido);
                    secuenciaPedido.add(rutaXPedido);
                }
            }*/
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

            //camionRepository.save(camionesDisponibles.get(i));

        }
        pedidoRepository.saveAll(pedidosNuevos);
        camionRepository.saveAll(camionesDisponibles);
        plantaRepository.saveAll(plantas);
        rutaXNodoRepository.saveAll(secuenciaRuta);
        rutaXPedidoRepository.saveAll(secuenciaPedido);
        rutaXPlantaRepository.saveAll(secuenciaPlanta);

        if(averiado)
            ConcurrentValues.allowFail.release();

        return solucion;
    }

    public ArrayList<Pedido> asignarPedidos3Dias(/*Fecha fecha, */ArrayList<Pedido> pedidos, int multiplier){
        StaticValues.numCamion = 1;
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
            pedidos.get(i).setFechaLimite(fechaTemp.plusHours(pedidos.get(i).getPlazoEntrega()));
            if(fechaTemp.isBefore(fechaInicio))
                fechaInicio = fechaTemp;
            if(pedidos.get(i).getFechaLimite().isAfter(fechaFin))
                fechaFin = pedidos.get(i).getFechaLimite();
        }
        pedidoRepository.saveAll(pedidos);

        //ArrayList<Pedido> pedidosActuales = pedidoRepository.listarPedidosDisponibles(fechaInicio, 2);

        //Creamos un thread para el algoritmo
        StaticValues.mult = multiplier;
        StaticValues.start = LocalDateTime.now(StaticValues.zoneId);
        StaticValues.virtualDate = fechaInicio.truncatedTo(ChronoUnit.DAYS);
        StaticValues.simulationType = 2;
        StaticValues.end = fechaFin;

        /*File log = new File("../logs/simulation/log"+LocalDateTime.now(StaticValues.zoneId)+".txt");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(log, true);
            OutputLog.logDaily = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        UpdateCurrentValues updating = applicationContext.getBean(UpdateCurrentValues.class);

        taskExecutor.execute(updating);

        FillDeposit fillDeposit = applicationContext.getBean(FillDeposit.class);

        taskExecutor.execute(fillDeposit);

        AlgorithmThread algorithm = applicationContext.getBean(AlgorithmThread.class);

        taskExecutor.execute(algorithm);

        return pedidos;

    }

    public ArrayList<Pedido> asignarPedidosColapso(/*Fecha fecha, */ArrayList<Pedido> pedidos, int multiplier){
        LocalDateTime fechaInicio = pedidos.get(0).getFechaPedido(), fechaTemp;
        LocalDateTime fechaFin = fechaInicio;
        int i;
        for(i = 0; i<pedidos.size();i++){
            pedidos.get(i).setEstadoPedido("Nuevo");
            Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(pedidos.get(i).getNodo().getCoordenadaX(),
                    pedidos.get(i).getNodo().getCoordenadaY());
            pedidos.get(i).getNodo().setId(nodo.getId());
            pedidos.get(i).setTipo(3);  //2 es simulacion 3 dias
            fechaTemp = pedidos.get(i).getFechaPedido();
            pedidos.get(i).setFechaLimite(fechaTemp.plusHours(pedidos.get(i).getPlazoEntrega()));
            if(fechaTemp.isBefore(fechaInicio))
                fechaInicio = fechaTemp;
            if(pedidos.get(i).getFechaLimite().isAfter(fechaFin))
                fechaFin = pedidos.get(i).getFechaLimite();
        }
        pedidoRepository.saveAll(pedidos);

        //ArrayList<Pedido> pedidosActuales = pedidoRepository.listarPedidosDisponibles(fechaInicio, 2);

        //Creamos un thread para el algoritmo
        StaticValues.mult = multiplier;
        StaticValues.start = LocalDateTime.now(StaticValues.zoneId);
        StaticValues.virtualDate = fechaInicio.truncatedTo(ChronoUnit.DAYS);
        StaticValues.simulationType = 3;
        StaticValues.end = fechaFin;

        /*File log = new File("../logs/simulation/log"+LocalDateTime.now(StaticValues.zoneId)+".txt");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(log, true);
            OutputLog.logDaily = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        UpdateCurrentValues updating = applicationContext.getBean(UpdateCurrentValues.class);

        taskExecutor.execute(updating);

        FillDeposit fillDeposit = applicationContext.getBean(FillDeposit.class);

        taskExecutor.execute(fillDeposit);

        AlgorithmThread algorithm = applicationContext.getBean(AlgorithmThread.class);

        taskExecutor.execute(algorithm);

        return pedidos;

    }


}
