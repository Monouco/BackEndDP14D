package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.*;
import com.grupo4D.sag_system.model.algorithm.Ant;
import com.grupo4D.sag_system.model.algorithm.DepositGLP;
import com.grupo4D.sag_system.model.algorithm.Mapa;
import com.grupo4D.sag_system.model.algorithm.Order;
import com.grupo4D.sag_system.model.algorithm.pathFinding.ACSAlgorithm;
import com.grupo4D.sag_system.model.response.*;
import com.grupo4D.sag_system.model.runnable.*;
import com.grupo4D.sag_system.model.statics.ConcurrentValues;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.*;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    private ThreadPoolTaskExecutor taskExecutor;

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
        try{
            List<Object[]> codigosCamion = camionRepository.listarCodigosCamionConID();
            HashMap<Integer, String> mapaCodigosCamion = new HashMap<Integer,String>();
            for (Object[] cod : codigosCamion){
                mapaCodigosCamion.put((int)cod[0],cod[1].toString());
            }
            ArrayList<TipoCamion> tiposDeCamion = tipoCamionRepository.findAllByActivoTrue();
            HashMap<Integer,TipoCamion> mapaTiposDeCamion = new HashMap<Integer, TipoCamion>();
            for (TipoCamion tc: tiposDeCamion){
                mapaTiposDeCamion.put(tc.getId(),tc);
            }

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
                camionHR.setCodigoCamion(mapaCodigosCamion.get(c.getId()));
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
                        if (r.getCamion().getId() == c.getId()) {
                            ArrayList<RutaXPedido> pedidosDeRuta = rutaXPedidoRepository.findRutaXPedidosByRuta(r.getId());
                            ArrayList<PedidoHRFront> pedidos = new ArrayList<>();
                            for (RutaXPedido rxp : pedidosDeRuta) {
                                //Pedido pedido1ruta = pedidoRepository.findPedidoByIdAndActivoTrue(rxp.getPedido().getId());
                                if (rxp.getPedido() == null) {
                                    System.out.print("Pedido es nulo");
                                    continue;
                                }
                                //System.out.print("pedido de RutaXPedido "+rxp.getPedido().getId()+"\n");
                                PedidoHRFront pedidoHR = new PedidoHRFront();
                                pedidoHR.setIdPedido(rxp.getPedido().getId());
                                pedidoHR.setCantidadGLP(rxp.getCantidadGLPEnviado());
                                UbicacionHRFront u = new UbicacionHRFront();
                                u.setX(rxp.getPedido().getNodo().getCoordenadaX());
                                u.setY(rxp.getPedido().getNodo().getCoordenadaY());
                                pedidoHR.setUbicacion(u);
                                String hora = String.format("%02d", rxp.getFechaEntrega().getHour());
                                String minutos = String.format("%02d", rxp.getFechaEntrega().getMinute());
                                String segundos = String.format("%02d", rxp.getFechaEntrega().getSecond());
                                pedidoHR.setHoraLlegada(hora + ":" + minutos + ":" + segundos);
                                LocalDateTime finAtencion = rxp.getFechaEntrega().plusMinutes(10);
                                hora = String.format("%02d", finAtencion.getHour());
                                minutos = String.format("%02d", finAtencion.getMinute());
                                segundos = String.format("%02d", finAtencion.getSecond());
                                pedidoHR.setHoraDeFinAtencion(hora + ":" + minutos + ":" + segundos);
                                //System.out.print("pedido "+pedido1ruta.getId()+"\n");
                                pedidos.add(pedidoHR);
                                cantGLPTransportado += rxp.getCantidadGLPEnviado();

                            }
                            camionHR.setNumPedidos(pedidos.size());
                            camionHR.setPedidos(pedidos);
                            camionHR.setCantGlpActual(cantGLPTransportado); //cantidad de glp entregado
                            camionHR.setIdRuta(r.getId());


                            //Se saca los nodos de una ruta en particular
                            ArrayList<RutaXNodo> nodosDeRuta = rutaXNodoRepository.listarRutaXNodosPorRuta(r.getId());
                            ArrayList<RutaXPlanta> nodosPorPlanta = rutaXPlantaRepository.listarRutaXPlantaPorRuta(r.getId());
                            int sec = 0; //indice para recorrer nodosPorPlanta
                            double glp = 0;
                            double petroleo = 0;
                            double distancia = 0;
                            double[] pesos = new double[nodosDeRuta.size()];
                            double[] cantPetroleoTanque = new double[nodosDeRuta.size()];
                            int salto = 0;

                            if (nodosDeRuta.size() > 0 && pedidosDeRuta.size() > 0) {
                                ArrayList<Integer> distancias = new ArrayList<>();
                                TipoCamion tCamion = mapaTiposDeCamion.get(c.getTipoCamion().getId());
                                cantPetroleoTanque[0] = tCamion.getCapacidadPetroleo();
                                pesos[0] = tCamion.getCapacidadGLP(); //comienza con el tanque lleno
                                //Calculo de peso en el camion
                                //System.out.println("Cantidad nodos de ruta: "+nodosDeRuta.size());
                                for (int k = 0; k < nodosDeRuta.size(); k++) { //por cada nodo de la ruta
                                    //hallar el petroleo aca
                                    //0 a mas es entrega de pedido
                                    //-4,-3 es planta principal
                                    //distancia x peso/150
                                    if (nodosDeRuta.get(k).getPedido() >= 0) { //si se entrega un pedido
                                        pesos[k] = (pesos[k - 1] -
                                                pedidosDeRuta.get(salto).getCantidadGLPEnviado()) * 0.5 + tCamion.getPesoTara();
                                        salto = salto++;
//                            } else if (nodosDeRuta.get(k).getPedido() == -1) { //si es un nodo camino
//                                pesos[k] = pesos[k - 1] * 0.5 + tCamion.getPesoTara();
//                            } else {                                         //si es una planta
//                                pesos[k] = tCamion.getCapacidadGLP() * 0.5 + tCamion.getPesoTara();      //se rellena el tanque
//                                cantPetroleoTanque[k] = tCamion.getCapacidadPetroleo();
//                            }
                                    } else { //si es un nodo camino o una planta
                                        if (k != 0) {
                                            pesos[k] = pesos[k - 1] * 0.5 + tCamion.getPesoTara(); //si es un nodo camino
                                        } else {
                                            pesos[k] = tCamion.getCapacidadGLP() * 0.5 + tCamion.getPesoTara();      //se rellena el tanque
                                            cantPetroleoTanque[k] = tCamion.getCapacidadPetroleo();
                                        }
                                        if (sec < nodosPorPlanta.size() && nodosDeRuta.get(k).getSecuencia() == nodosPorPlanta.get(sec).getSecuencia()) { //esta en una planta
                                            pesos[k] = tCamion.getCapacidadGLP() * 0.5 + tCamion.getPesoTara();      //se rellena el tanque
                                            cantPetroleoTanque[k] = tCamion.getCapacidadPetroleo();   //se rellena el tanque
                                            sec = sec++;
                                        }
                                    }
                                    // System.out.println("Nodos de ruta: "+nodosDeRuta.get(k).getPedido());
                                }
                                double petroleoConsumido = 0;
                                double consumoTramoFinal = 0;
                                double cantPetroleoFinalRuta = 0;
                                int ind = Arrays.asList(petroleoConsumido).lastIndexOf(tCamion.getCapacidadPetroleo());
                                for (int y = 0; y < pesos.length; y++) {
                                    petroleoConsumido += pesos[y] / 150;
                                    if (y >= ind) {
                                        consumoTramoFinal += pesos[y] / 150;
                                    }
                                }
                                cantPetroleoFinalRuta = tCamion.getCapacidadPetroleo() - consumoTramoFinal;
                                camionHR.setCantPetroleoFinalRuta(cantPetroleoFinalRuta);
                                camionHR.setCantPetroleoActual(petroleoConsumido);
                            }
                        }

                    }

                }
                if (camionHR.getNumPedidos() > 0) {
                    hojaDeRuta.add(camionHR);
                }
            }

            return hojaDeRuta;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
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
            fill.setCode(camionRepository.listarCodigo1Camion(key));
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
        Planta planta2 = plantaRepository.listarGLPRestanteXPlanta(2);
        Planta planta3 = plantaRepository.listarGLPRestanteXPlanta(3);
        switch (tipo){
            case 1:{
                apiResponse.setGlpRestantePlanta2(planta2.getGlpDisponible());
                apiResponse.setGlpRestantePlanta3(planta3.getGlpDisponible());
                break;
            }
            case 2:{
                apiResponse.setGlpRestantePlanta2(planta2.getGlpDisponibleSimulacion());
                apiResponse.setGlpRestantePlanta3(planta3.getGlpDisponibleSimulacion());
                break;
            }
            case 3:{
                apiResponse.setGlpRestantePlanta2(planta2.getGlpDisponibleColapso());
                apiResponse.setGlpRestantePlanta3(planta3.getGlpDisponibleColapso());
                break;
            }
        }


        return apiResponse;
    }

    public ArrayList<RutaFront> asignarPedidos(LocalDateTime fecha, ArrayList<Pedido> pedidosNuevos, int tipo, long desfase, int multiplier){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
        LocalDateTime horaInicio = fecha;

        ArrayList<RutaFront> solucion = new ArrayList<>();

        if (pedidosNuevos.isEmpty()){
            return null;
        }

        //ver camiones disponibles
        ArrayList<Camion> camionesDisponibles = new ArrayList<>();
        if(tipo != 1)
            camionesDisponibles = camionRepository.listarCamionesTipo("Operativo", tipo); //Cambiar estado de double a string en BD y el resto
        else
            camionesDisponibles = camionRepository.listarCamionesTipoAsc("Operativo", tipo);

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

        mapa1.initializeCurrentRoadBlocks(fecha, fecha.plusHours(18), tipo);


        //revisar
        int numAlmacenes = 3;
        int numOrdenes = pedidosNuevos.size();
        int hTurno = 24;
        int cycles = 500;
        int steps = 100;
        int tiempoAtencion = 600;
        long nanos = 1000000000;
        double velocity = (double)10/36;
        double evaporationRate = 0.3;
        int averiado = 0;
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

        ACSAlgorithm algoritmoACS  = new ACSAlgorithm(numAlmacenes, numOrdenes,mapa1.getPlantaPrincipal(),hTurno, fecha, 0.5);

        hormigas = algoritmoACS.findSolution(hormigas,ordenes,mapa1,cycles,steps, camionesDisponibles.size(),evaporationRate);

        if (algoritmoACS.getHighestNum() != pedidosNuevos.size())
            System.out.println("No se atendieron todos los pedidos para " + tipo);

        System.out.println("Se tienen " + camionesDisponibles.size() + " camiones disponibles para tipo " + tipo);
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
            ruta.setActivo(true);
            ruta.setCostoOperacion(algoritmoACS.getFullOpCost().get(i));
            //ruta.setFechaInicio(horaInicio.plusSeconds(20)+);

            //Dependiendo del camion, generamos la averia si es que es simulacion de 3 dias
            if(tipo == 2) {
                switch (StaticValues.numCamion) {
                    case (2): {
                        StaticValues.simulationType = tipo;
                        StaticValues.idCamion = camionesDisponibles.get(i).getId();
                        StaticValues.virtualDate = fecha;
                        StaticValues.start = fecha.plusHours(2);
                        StaticValues.mult = multiplier;
                        averiado++;
                        AveriaScheduled averia = applicationContext.getBean(AveriaScheduled.class);
                        taskExecutor.execute(averia);
                        break;
                    }
                    case (4): {
                        StaticValues.simulationType = tipo;
                        StaticValues.idCamion = camionesDisponibles.get(i).getId();
                        StaticValues.virtualDate = fecha;
                        StaticValues.start = fecha.plusHours(3);
                        StaticValues.mult = multiplier;
                        averiado++;
                        AveriaScheduled averia = applicationContext.getBean(AveriaScheduled.class);
                        taskExecutor.execute(averia);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                StaticValues.numCamion++;
            }

            //rutaRepository.save(ruta);

            ArrayList<NodoFront> path = new ArrayList<>();
            int k = 0, atendidos = 0,  temp = 0;
            ArrayList<Integer> hBestSolution = hormigas.get(i).getBestSolution();
            ArrayList<Double> hBestGLP = hormigas.get(i).getBestSolutionGLP();
            ArrayList<Double> hBestFuel = hormigas.get(i).getBestSolutionFuel();
            for (int[]j:hormigas.get(i).getBestRoute()) {
                Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(j[0],j[1]);
                RutaXNodo rutaXNodo = new RutaXNodo();
                rutaXNodo.setNodo(nodo);
                rutaXNodo.setRuta(ruta);
                rutaXNodo.setSecuencia(k);


                //respuesta para el front
                NodoFront nodoFront = new NodoFront();
                nodoFront.setX(j[0]);
                nodoFront.setY(j[1]);
                if(j.length >=3){
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
                            rutaXPedido.setCostoOperacion(hBestFuel.get(temp));
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

                            //Verificar si esto no causa errores, esto es para que el pedido tenga todo el costo de lo que le costo llegar hasta ahi (incluyendo recarga)
                            hBestFuel.set(temp + 1, hBestFuel.get(temp) + hBestFuel.get(temp+1));
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
            if(temp == 0){
                System.out.println("Error en la ejecucion, no se pudo aumentar el contador");
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
        try {
            rutaXNodoRepository.saveAll(secuenciaRuta);
            if(secuenciaPedido.size()==0){
                System.out.println("Error, no se ha puesto ruta por nodo");
            }
            if(secuenciaPlanta.size()==0){
                System.out.println("Error, no se ha puesto ruta por planta");
            }
            rutaXPlantaRepository.saveAll(secuenciaPlanta);
            rutaXPedidoRepository.saveAll(secuenciaPedido);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        if(averiado > 0)
            ConcurrentValues.allowFail.release(averiado);

        return solucion;
    }

    public ArrayList<Pedido> asignarPedidos3Dias(/*Fecha fecha, */ArrayList<Pedido> pedidos, int multiplier) throws InterruptedException {
        StaticValues.numCamion = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
        LocalDateTime fechaInicio = pedidos.get(0).getFechaPedido(), fechaTemp;
        LocalDateTime fechaFin = fechaInicio;
        HashMap<String,Nodo> listaNodos = new HashMap<>();
        ArrayList<Nodo> nodosAct = nodoRepository.listarNodos();

        for (Nodo n:
             nodosAct) {
            listaNodos.put(n.getCoor(),n);
        }

        int i;
        for(i = 0; i<pedidos.size();i++){
            pedidos.get(i).setEstadoPedido("Nuevo");
            /*Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(pedidos.get(i).getNodo().getCoordenadaX(),
                    pedidos.get(i).getNodo().getCoordenadaY());*/
            Nodo nodo = listaNodos.get(pedidos.get(i).getNodo().getCoor());
            pedidos.get(i).getNodo().setId(nodo.getId());
            pedidos.get(i).setTipo(2);  //2 es simulacion 3 dias
            fechaTemp = pedidos.get(i).getFechaPedido();
            pedidos.get(i).setFechaLimite(fechaTemp.plusHours(pedidos.get(i).getPlazoEntrega()));
            if(fechaTemp.isBefore(fechaInicio))
                fechaInicio = fechaTemp;
            /*if(pedidos.get(i).getFechaLimite().isAfter(fechaFin))
                fechaFin = pedidos.get(i).getFechaLimite();*/
            if(pedidos.get(i).getFechaPedido().isAfter(fechaFin))
                fechaFin = pedidos.get(i).getFechaPedido();
        }

        pedidoRepository.saveAll(pedidos);

        //ArrayList<Pedido> pedidosActuales = pedidoRepository.listarPedidosDisponibles(fechaInicio, 2);

        //Creamos un thread para el algoritmo
        StaticValues.mult = multiplier;
        StaticValues.start = LocalDateTime.now(StaticValues.zoneId);
        StaticValues.virtualDate = fechaInicio.truncatedTo(ChronoUnit.DAYS);
        StaticValues.simulationType = 2;
        StaticValues.end = fechaFin.plusHours(2);

        /*File log = new File("../logs/simulation/log"+LocalDateTime.now(StaticValues.zoneId)+".txt");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(log, true);
            OutputLog.logDaily = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        AlgorithmThread algorithm = applicationContext.getBean(AlgorithmThread.class);

        taskExecutor.execute(algorithm);

        return pedidos;

    }

    public ArrayList<Pedido> asignarPedidosColapso(/*Fecha fecha, */ArrayList<Pedido> pedidos, int multiplier) throws InterruptedException {
        LocalDateTime fechaInicio = pedidos.get(0).getFechaPedido(), fechaTemp;
        LocalDateTime fechaFin = fechaInicio;

        HashMap<String,Nodo> listaNodos = new HashMap<>();
        ArrayList<Nodo> nodosAct = nodoRepository.listarNodos();

        for (Nodo n:
                nodosAct) {
            listaNodos.put(n.getCoor(),n);
        }

        int i;
        for(i = 0; i<pedidos.size();i++){
            pedidos.get(i).setEstadoPedido("Nuevo");
            /*Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(pedidos.get(i).getNodo().getCoordenadaX(),
                    pedidos.get(i).getNodo().getCoordenadaY());*/
            Nodo nodo = listaNodos.get(pedidos.get(i).getNodo().getCoor());
            pedidos.get(i).getNodo().setId(nodo.getId());
            pedidos.get(i).setTipo(3);  //2 es simulacion 3 dias
            fechaTemp = pedidos.get(i).getFechaPedido();
            pedidos.get(i).setFechaLimite(fechaTemp.plusHours(pedidos.get(i).getPlazoEntrega()));
            if(fechaTemp.isBefore(fechaInicio))
                fechaInicio = fechaTemp;
            /*if(pedidos.get(i).getFechaLimite().isAfter(fechaFin))
                fechaFin = pedidos.get(i).getFechaLimite();*/
            if(pedidos.get(i).getFechaPedido().isAfter(fechaFin))
                fechaFin = pedidos.get(i).getFechaPedido();
        }
        /*
        StaticValues.simulationType = 3;
        StaticValues.ordersCollapse = pedidos;
        InsertPedidosThread insertPedidos = applicationContext.getBean(InsertPedidosThread.class);

        taskExecutor.execute(insertPedidos);

        TimeUnit.SECONDS.sleep(3);*/

        pedidoRepository.saveAll(pedidos);

        //ArrayList<Pedido> pedidosActuales = pedidoRepository.listarPedidosDisponibles(fechaInicio, 2);

        //Creamos un thread para el algoritmo
        StaticValues.mult = multiplier;
        StaticValues.start = LocalDateTime.now(StaticValues.zoneId);
        StaticValues.virtualDate = fechaInicio.truncatedTo(ChronoUnit.DAYS);
        StaticValues.simulationType = 3;
        StaticValues.end = fechaFin.plusHours(2);

        /*File log = new File("../logs/simulation/log"+LocalDateTime.now(StaticValues.zoneId)+".txt");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(log, true);
            OutputLog.logDaily = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*UpdateCurrentValues updating = applicationContext.getBean(UpdateCurrentValues.class);

        taskExecutor.execute(updating);

        FillDeposit fillDeposit = applicationContext.getBean(FillDeposit.class);

        taskExecutor.execute(fillDeposit);*/

        AlgorithmThread algorithm = applicationContext.getBean(AlgorithmThread.class);

        taskExecutor.execute(algorithm);

        return pedidos;

    }

    public Fecha fechaSimulacion(int tipo, int velocidad){
        LocalDateTime result = LocalDateTime.now(StaticValues.zoneId);
        long nanos = 1000000000;
        long nanosDiff = 0;
        switch (tipo){
            case 1: {
                return new Fecha(result,tipo,velocidad);
            }
            case 2: {
                if (StaticValues.simRealTime != null) {
                    nanosDiff = ChronoUnit.NANOS.between(StaticValues.simRealTime, result);
                    result = StaticValues.simInTime;
                }

            }
            case 3: {
                if (StaticValues.collapseRealTime != null) {
                    nanosDiff = ChronoUnit.NANOS.between(StaticValues.collapseRealTime, result);
                    result = StaticValues.collapseInTime;
                }
            }
        }

        result = result.plusNanos(nanosDiff * velocidad);

        return new Fecha((nanosDiff != 0) ? result : null,tipo,velocidad);
    }

    public HojaRutaFront hojaRutaNodos(int idRuta){
        HojaRutaFront hojaRutaFront = new HojaRutaFront();
        ArrayList<NodoHojaRutaFront> nodosHojaRuta = new ArrayList<>();
        ArrayList<NodoHojaRutaFront> lista = new ArrayList<>();
        ArrayList<String> nodos = new ArrayList<>();
        ArrayList<String> tipos = new ArrayList<>();
        ArrayList<String> fechas = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String route;
        String dir = "";
        String curDir = "";
        String tipo = "";
        String fecha = "";
        int [] coorAnt = {-1,-1};
        int [] curCoor = new int[2]; // -2 -1 1 2
        ArrayList<RutaXNodo> nodosDeRuta = rutaXNodoRepository.listarRutaXNodosPorRuta(idRuta);
        ArrayList<RutaXPlanta> plantas = rutaXPlantaRepository.listarRutaXPlantaPorRuta(idRuta);
        ArrayList<RutaXPedido> pedidos = rutaXPedidoRepository.findRutaXPedidosByRuta(idRuta);
        HashMap<Integer,RutaXPlanta> mapaPlantas = new HashMap<Integer,RutaXPlanta>();
        HashMap<Integer,RutaXPedido> mapaPedidos = new HashMap<Integer,RutaXPedido>();
        HashMap<Integer,Integer> mapa = new HashMap<Integer,Integer>();
        for (RutaXPlanta rxp: plantas){
            mapa.put(rxp.getSecuencia(),rxp.getPlanta().getId());
            mapaPlantas.put(rxp.getSecuencia(),rxp);
        }
        for (RutaXPedido rxp: pedidos){
            mapa.put(rxp.getSecuencia(),rxp.getPedido().getId());
            mapaPedidos.put(rxp.getSecuencia(),rxp);
        }

        for (RutaXNodo r:
                nodosDeRuta) {

            curCoor[0] = r.getNodo().getCoordenadaX();
            curCoor[1] = r.getNodo().getCoordenadaY();
            //Caso planta inicial
            if(coorAnt[0] == -1 && coorAnt[1] == -1){
                coorAnt[0] = curCoor[0];
                coorAnt[1] = curCoor[1];
                route = coorAnt[0] + ", " + coorAnt[1];
                nodos.add(route);
                tipo = "-";
                tipos.add(tipo);
                fecha = "-";
                fechas.add(fecha);
                continue;
            }


            switch (2*(curCoor[0]-coorAnt[0]) + (curCoor[1]-coorAnt[1])){
                case -2:{
                    curDir = "l";
                    break;
                }
                case -1:{
                    curDir = "u";
                    break;
                }
                case 1:{
                    curDir = "r";
                    break;
                }
                case 2:{
                    curDir = "d";
                    break;
                }
                default:{
                    break;
                }
            }

            if(dir == ""){
                dir = curDir;
            }
            //Caso esquinas
            if(dir != curDir){
                route = coorAnt[0] + ", " + coorAnt[1];
                dir = curDir;
                nodos.add(route);

                int i = nodosDeRuta.indexOf(r);
                if (nodosDeRuta.get(i-1).getPedido()>=0){ //Si es un pedido
                    tipo = "Pedido " + mapa.get(nodosDeRuta.get(i-1).getSecuencia());
                    tipos.add(tipo);
                    fecha = mapaPedidos.get(nodosDeRuta.get(i-1).getSecuencia()).getFechaEntrega().format(formatter);
                    fechas.add(fecha);

                }else if (mapa.containsKey(nodosDeRuta.get(i-1).getSecuencia())){ //si no es un pedido pero si una planta
                    tipo = "Planta " + mapa.get(nodosDeRuta.get(i-1).getSecuencia()) ;
                    tipos.add(tipo);
                    fecha = mapaPlantas.get(nodosDeRuta.get(i-1).getSecuencia()).getFechaLLegada().format(formatter);
                    fechas.add(fecha);
                }else{  //si es solo esquina
                    tipo = "-";
                    tipos.add(tipo);
                    fecha = "-";
                    fechas.add(fecha);
                }
            }else if (r.getPedido()>=0){ //Caso pedidos
                route = curCoor[0] + ", " + curCoor[1];
                nodos.add(route);
                tipo = "Pedido " + mapa.get(r.getSecuencia());
                tipos.add(tipo);
                fecha = mapaPedidos.get(r.getSecuencia()).getFechaEntrega().format(formatter);
                fechas.add(fecha);
            }else if (mapa.containsKey(r.getSecuencia())){ //Caso plantas
                route = curCoor[0] + ", " + curCoor[1];
                nodos.add(route);
                tipo = "Planta " + mapa.get(r.getSecuencia());
                tipos.add(tipo);
                fecha = mapaPlantas.get(r.getSecuencia()).getFechaLLegada().format(formatter);
                fechas.add(fecha);
            }
            coorAnt[0] = curCoor[0];
            coorAnt[1] = curCoor[1];
            //Caso planta final
            if (nodosDeRuta.indexOf(r) == nodosDeRuta.size()-1){
                route = coorAnt[0] + ", " + coorAnt[1];
                nodos.add(route);
                tipo = "Planta Principal";
                tipos.add(tipo);
                fecha = "-";
                fechas.add(fecha);
            }

        }


        if (nodos.size()== tipos.size() && tipos.size()==fechas.size()) {
            for (int j = 1; j < nodos.size(); j++) {
               // System.out.println(j);
                NodoHojaRutaFront n = new NodoHojaRutaFront();
                n.setInicio(nodos.get(j - 1));
                n.setLlegada(nodos.get(j));
                //Llenar si es planta o pedido o nada
                n.setTipo(tipos.get(j));
                nodosHojaRuta.add(n);
                n.setFecha(fechas.get(j));
//                System.out.println(nodos.get(j));
//                System.out.println(tipos.get(j-1)+"\n");
            }

            for (NodoHojaRutaFront n : nodosHojaRuta){
                if (n.getInicio().equals(n.getLlegada())){
                    continue;
                }else{
                    lista.add(n);
                }
            }
        }
        hojaRutaFront.setNodos(lista);
        return hojaRutaFront;
    }


}
