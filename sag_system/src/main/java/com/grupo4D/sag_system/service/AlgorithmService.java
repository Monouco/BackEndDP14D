package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.*;
import com.grupo4D.sag_system.model.algorithm.Ant;
import com.grupo4D.sag_system.model.algorithm.DepositGLP;
import com.grupo4D.sag_system.model.algorithm.Mapa;
import com.grupo4D.sag_system.model.algorithm.Order;
import com.grupo4D.sag_system.model.algorithm.pathFinding.ACSAlgorithm;
import com.grupo4D.sag_system.model.response.NodoFront;
import com.grupo4D.sag_system.model.response.RutaFront;
import com.grupo4D.sag_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //public ArrayList<RutaFront> asignarPedidos(LocalDateTime horaInicio){
    //public ArrayList<RutaFront> asignarPedidos(String fecha){
    public ArrayList<RutaFront> asignarPedidos(Fecha fecha){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
        //System.out.print("INICIO\n" +fecha+"\nFECHA RECIBIDA\n");
        //String[] fechas = fecha.split(":");
       //System.out.print("INICIO\n" +fechas[0]+"\nFECHA RECIBIDA\n");
        //System.out.print("INICIO\n" +fechas[1]+"\nFECHA RECIBIDA\n");
        //LocalDateTime horaInicio = LocalDateTime.parse(fecha, formatter);
        LocalDateTime horaInicio = fecha.getFecha();

        ArrayList<RutaFront> solucion = new ArrayList<>();

        ArrayList<Pedido> pedidosNuevos = new ArrayList<>();
        pedidosNuevos = pedidoRepository.findPedidosByEstadoPedido("Nuevo");

        if (pedidosNuevos.isEmpty()){
            return null;
        }

        //ver camiones disponibles
        ArrayList<Camion> camionesDisponibles = new ArrayList<>();
        camionesDisponibles = camionRepository.findCamionsByEstadoAndActivoTrue(1); //Cambiar estado de double a string en BD y el resto

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
                    pedidosNuevos.get(i).getPlazoEntrega());
            ordenes.add(orden);
        }

        ACSAlgorithm algoritmoACS  = new ACSAlgorithm(numAlmacenes, numOrdenes,mapa1.getPlantaPrincipal(),hTurno);

        hormigas = algoritmoACS.findSolution(hormigas,ordenes,mapa1,cycles,steps, camionesDisponibles.size(),evaporationRate);

        //hormigas a camiones
        for (int i =0; i< camionesDisponibles.size();i++){
            Ruta ruta = new Ruta();
            ruta.setCamion(camionesDisponibles.get(i));

            ruta.setFechaInicio(horaInicio.plusMinutes(1));
            rutaRepository.save(ruta);

            ArrayList<NodoFront> path = new ArrayList<>();

            for (int[]j:hormigas.get(i).getBestRoute()) {
                Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(j[0],j[1]);
                RutaXNodo rutaXNodo = new RutaXNodo();
                rutaXNodo.setNodo(nodo);
                rutaXNodo.setRuta(ruta);
                if(j.length ==3){
                    rutaXNodo.setPedido(j[2]);
                }else{
                    rutaXNodo.setPedido(-1);
                }
                rutaXNodoRepository.save(rutaXNodo);

                //respuesta para el front
                NodoFront nodoFront = new NodoFront();
                nodoFront.setX(j[0]);
                nodoFront.setY(j[1]);
                if(j.length ==3){
                    nodoFront.setPedido(j[2]);
                }else{
                    nodoFront.setPedido(-1);
                }
                path.add(nodoFront);
            }

            RutaFront rutaFront = new RutaFront(ruta.getId(), ruta.getFechaInicio(), path);
            for (int j: hormigas.get(i).getBestSolution()) {
                if (j>=0){
                    RutaXPedido rutaXPedido = new RutaXPedido();
                    rutaXPedido.setPedido(pedidosNuevos.get(j));
                    rutaXPedido.setRuta(ruta);
                    rutaXPedidoRepository.save(rutaXPedido);
                }
            }
            rutaFront.setPath(path);
            solucion.add(rutaFront);
        }
        return solucion;
    }
}
