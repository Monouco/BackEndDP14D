package com.grupo4D.sag_system.model.runnable;

import com.grupo4D.sag_system.SagSystemApplication;
import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.response.TipoSimulacionFront;
import com.grupo4D.sag_system.model.statics.ConcurrentValues;
import com.grupo4D.sag_system.model.statics.OutputLog;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.CamionRepository;
import com.grupo4D.sag_system.repository.PedidoRepository;
import com.grupo4D.sag_system.repository.PlantaRepository;
import com.grupo4D.sag_system.service.AlgorithmService;
import com.grupo4D.sag_system.service.PedidoService;
import com.grupo4D.sag_system.service.ReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Component
@Scope("prototype")
public class AlgorithmThread implements Runnable {

    private int multiplier;
    private LocalDateTime simulationDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BufferedWriter log;
    private int type;
    private long offset;

    @Autowired
    AlgorithmService algorithmService;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoService pedidoService;

    @Autowired
    ReportesService reportesService;

    @Autowired
    CamionRepository camionRepository;

    @Autowired
    PlantaRepository plantaRepository;
    /*
    public AlgorithmThread(int multiplier, LocalDateTime simulationDate, LocalDateTime startDate, int type){
        this.multiplier = multiplier;
        this.simulationDate = simulationDate;
        this.startDate = startDate;
        this.type = type;
    }*/

    @Override
    public void run(){

        this.multiplier = StaticValues.mult;
        this.startDate = StaticValues.start;
        this.simulationDate = StaticValues.virtualDate;
        this.endDate = StaticValues.end;
        this.type = StaticValues.simulationType;
        this.offset = ChronoUnit.NANOS.between(startDate,simulationDate);
        //this.log = OutputLog.logDaily;
        StaticValues.end = null;

        LocalDateTime nextDay = simulationDate.plusDays(1).truncatedTo(ChronoUnit.DAYS);

        System.out.println(multiplier);
        //Considerando 10 min
        //long sleepTime = 600000;
        //Considerando 3 min para dia a dia y 3 segundos para colapso y simulacion
        long sleepTime ;

        if(type == 1){
            sleepTime = 300000;
        }
        else{
            sleepTime = 3000;
        }

        if (endDate != null) this.endDate = this.endDate.plusSeconds(sleepTime/1000*multiplier);
        switch (type){
            case 1: {
                StaticValues.collapseFlag = false;
                StaticValues.comCollapseFlag = false;
                break;
            }
            case 2: {
                StaticValues.collapseSimulationFlag = false;
                StaticValues.comSimCollapseFlag = false;
                break;
            }
            case 3: {
                StaticValues.fullCollapseFlag = false;
                StaticValues.comFullCollapseFlag = false;
                break;
            }
        }
        ArrayList<Pedido> orders;

        try{
            //
            while (true) {
                //System.out.println(multiplier + 2);
                if(endDate != null && simulationDate.isAfter(endDate)){
                    StaticValues.endSimulationFlag = true;
                    break;
                }

                camionRepository.updatingValues(simulationDate, type, ChronoUnit.NANOS.between(LocalDateTime.now(StaticValues.zoneId),simulationDate));

                orders = pedidoService.listarPedidosDisponibles(simulationDate, type);

                if (!orders.isEmpty()) {
                    //Colapso logistico va aqui
                    for (Pedido order:
                         orders) {
                        if(simulationDate.isAfter(order.getFechaLimite())){
                            //Calculamos la cantidad de glp por entregar
                            double leftGLP = 0;
                            for (Pedido o:
                                 orders) {
                                leftGLP += (o.getCantidadGLP() - o.getGlpProgramado());
                            }
                            int nTypeA = (int)(leftGLP/25);
                            leftGLP -= nTypeA*25;
                            int nTypeB = (int)(leftGLP/15);
                            leftGLP -= nTypeB*15;
                            int nTypeC = (int)(leftGLP/10);
                            leftGLP -= nTypeC*10;
                            int nTypeD = (int)Math.ceil(leftGLP/5);
                            String toUpgrade = "Se necesitaran: \n";

                            nTypeA = (int)Math.ceil(nTypeA*0.01);
                            nTypeB = (int)Math.ceil(nTypeB*0.01);
                            nTypeC = (int)Math.ceil(nTypeC*0.01);
                            nTypeD = (int)Math.ceil(nTypeC*0.01);
                            if(nTypeA >0)
                                toUpgrade = toUpgrade  + nTypeA + " camiones del Tipo A \n";
                            if(nTypeB >0)
                                toUpgrade = toUpgrade +  nTypeB + " camiones del Tipo B \n";
                            if(nTypeC >0)
                                toUpgrade = toUpgrade +  nTypeC + " camiones del Tipo C \n";
                            if(nTypeD >0)
                                toUpgrade = toUpgrade +  nTypeD + " camiones del Tipo D \n";

                            switch (type){
                                case 1: {
                                    StaticValues.collapseFlag = true;
                                    StaticValues.comCollapseFlag = true;
                                    StaticValues.collapseStatus.setFechaColapso(simulationDate);
                                    StaticValues.collapseStatus.setPedidosAtendidos(pedidoRepository.pedidosAtendidos(type));
                                    StaticValues.collapseStatus.setPedidosPorAtender(pedidoRepository.pedidosPorAtendidos(type));
                                    StaticValues.collapseStatus.setHojaRuta(algorithmService.obtenerHojaDeRuta(new TipoSimulacionFront(type,multiplier)));
                                    StaticValues.collapseStatus.setPedidosEnCola(orders);
                                    StaticValues.collapseStatus.setFlotaFaltante(toUpgrade);
                                    StaticValues.reportCapacity = reportesService.reporteCapacidadAtencion(type);
                                    System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Colapso Logistico tipo " + type + " a las " + simulationDate);
                                    break;
                                }
                                case 2: {
                                    StaticValues.collapseSimulationFlag = true;
                                    StaticValues.comSimCollapseFlag = true;
                                    StaticValues.collapseSimStatus.setFechaColapso(simulationDate);
                                    StaticValues.collapseSimStatus.setPedidosAtendidos(pedidoRepository.pedidosAtendidos(type));
                                    StaticValues.collapseSimStatus.setPedidosPorAtender(pedidoRepository.pedidosPorAtendidos(type));
                                    StaticValues.collapseSimStatus.setHojaRuta(algorithmService.obtenerHojaDeRuta(new TipoSimulacionFront(type,multiplier)));
                                    StaticValues.collapseSimStatus.setPedidosEnCola(orders);
                                    StaticValues.collapseSimStatus.setFlotaFaltante(toUpgrade);
                                    StaticValues.simReportCapacity = reportesService.reporteCapacidadAtencion(type);
                                    System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Colapso Logistico tipo " + type + " a las " + simulationDate);
                                    break;
                                }
                                case 3: {
                                    StaticValues.fullCollapseFlag = true;
                                    StaticValues.comFullCollapseFlag = true;
                                    StaticValues.fullCollapseStatus.setFechaColapso(simulationDate);
                                    StaticValues.fullCollapseStatus.setPedidosAtendidos(pedidoRepository.pedidosAtendidos(type));
                                    StaticValues.fullCollapseStatus.setPedidosPorAtender(pedidoRepository.pedidosPorAtendidos(type));
                                    System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Entrando a colapso " + type + " a las " + simulationDate);
                                    StaticValues.fullCollapseStatus.setHojaRuta(algorithmService.obtenerHojaDeRuta(new TipoSimulacionFront(type,multiplier)));
                                    StaticValues.fullCollapseStatus.setPedidosEnCola(orders);
                                    StaticValues.fullCollapseStatus.setFlotaFaltante(toUpgrade);
                                    StaticValues.collapseReportCapacity = reportesService.reporteCapacidadAtencion(type);
                                    System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Colapso Logistico tipo " + type + " a las " + simulationDate);
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    if(StaticValues.collapseFlag && type == 1){
                        System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Terminando simulacion " + type + " por colapso a las " + simulationDate);
                        /*ConcurrentValues.freeUpdateVal.acquire();
                        ConcurrentValues.updateVal.release();*/
                        break;
                    }
                    if(StaticValues.collapseSimulationFlag && type == 2){
                        System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Terminando simulacion " + type + " por colapso a las " + simulationDate);
                        /*ConcurrentValues.freeUpdateValSimulation.acquire();
                        ConcurrentValues.updateValSimulation.release();*/
                        break;
                    }
                    if(StaticValues.fullCollapseFlag && type == 3){
                        System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Terminando simulacion " + type + " por colapso a las " + simulationDate);
                        /*ConcurrentValues.freeUpdateValCollapse.acquire();
                        ConcurrentValues.updateValCollapse.release();*/
                        break;
                    }

                    algorithmService.asignarPedidos(simulationDate, orders, type, offset, multiplier);

                    /*switch (type){
                        case 1: {
                            try {
                                ConcurrentValues.freeUpdateVal.acquire();
                                algorithmService.asignarPedidos(simulationDate, orders, type, offset, multiplier);
                                ConcurrentValues.updateVal.release();
                            }
                            catch (Exception e){
                                System.out.println(e.getMessage());
                                ConcurrentValues.updateVal.release();
                            }
                            break;
                        }
                        case 2: {
                            try {
                                ConcurrentValues.freeUpdateValSimulation.acquire();
                                algorithmService.asignarPedidos(simulationDate, orders, type, offset, multiplier);
                                ConcurrentValues.updateValSimulation.release();
                            }catch (Exception e){
                                System.out.println(e.getMessage());
                                ConcurrentValues.updateValSimulation.release();
                            }
                            break;
                        }
                        case 3: {
                            try {
                                ConcurrentValues.freeUpdateValCollapse.acquire();
                                algorithmService.asignarPedidos(simulationDate, orders, type, offset, multiplier);
                                ConcurrentValues.updateValCollapse.release();
                            }catch (Exception e){
                                System.out.println(e.getMessage());
                                ConcurrentValues.updateValCollapse.release();
                            }
                            break;
                        }
                    }*/

                    System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Pedidos atendidos para el tipo " + type + "Tiempo de simulacion " + simulationDate);
                    //log.write(LocalDateTime.now(StaticValues.zoneId) + " Pedidos atendidos para el tipo " + type + "Tiempo de simulacion " + simulationDate + '\n');

                } else {
                    switch (type){
                        case 2: {
                            StaticValues.simInTime = simulationDate;
                            StaticValues.simRealTime = startDate;
                            break;
                        }
                        case 3: {
                            StaticValues.collapseInTime = simulationDate;
                            StaticValues.collapseRealTime = startDate;
                            break;
                        }
                    }
                    System.out.println("No hubieron pedidos para el tipo " + type + " Tiempo de simulacion " + simulationDate);
                    //log.write("No hubieron pedidos para el tipo " + type + " Tiempo de simulacion " + simulationDate + '\n');
                }
                this.simulationDate = (type ==1) ? LocalDateTime.now(StaticValues.zoneId).plusSeconds(sleepTime / 1000 * multiplier) : this.simulationDate.plusSeconds(sleepTime / 1000 * multiplier);
                //this.startDate = this.startDate.plusSeconds(sleepTime/1000);
                this.startDate = LocalDateTime.now(StaticValues.zoneId).plusSeconds(sleepTime / 1000 );
                //this.offset = this.offset  + sleepTime  * 1000000 * (multiplier - 1);
                this.offset = ChronoUnit.NANOS.between(startDate,simulationDate);

                //Esto es para que se vuelva a correr el algoritmo
                try {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e){
                    System.out.println(e.getMessage());
                }

                if(type != 1) {
                    if (simulationDate.isAfter(nextDay)) {
                        /*switch (type){
                            case 2: {
                                ConcurrentValues.newSimulationDay.release();
                                break;
                            }
                            case 3: {
                                ConcurrentValues.newCollapseDay.release();
                                break;
                            }
                        }*/
                        plantaRepository.fillDeposit(type);
                        nextDay = simulationDate.plusDays(1).truncatedTo(ChronoUnit.DAYS);
                    }
                }

            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        /*switch (type){
            case 2: {
                ConcurrentValues.newSimulationDay.release();
                break;
            }
            case 3: {
                ConcurrentValues.newCollapseDay.release();
                break;
            }
        }*/

        System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Terminando la ejecucion del algoritmo tipo " + type );
        /*try {
            log.write(LocalDateTime.now(StaticValues.zoneId) + " Terminando la ejecucion del algoritmo tipo " + type +'\n');
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //Reiniciamos todo lo utilizado por la simulacion, y lo agregado

        if(type != 1)
            pedidoRepository.terminarSimulacion(type);

        switch (type){
            case 2: {
                StaticValues.simInTime = null;
                StaticValues.simRealTime = null;
                break;
            }
            case 3: {
                StaticValues.collapseInTime = null;
                StaticValues.collapseRealTime = null;
                break;
            }
        }

    }

}
