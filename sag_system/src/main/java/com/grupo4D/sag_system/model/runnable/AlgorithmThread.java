package com.grupo4D.sag_system.model.runnable;

import com.grupo4D.sag_system.SagSystemApplication;
import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.statics.ConcurrentValues;
import com.grupo4D.sag_system.model.statics.OutputLog;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.PedidoRepository;
import com.grupo4D.sag_system.service.AlgorithmService;
import com.grupo4D.sag_system.service.PedidoService;
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
            sleepTime = 180000;
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

                orders = pedidoService.listarPedidosDisponibles(simulationDate, type);

                if (!orders.isEmpty()) {
                    //Colapso logistico va aqui
                    for (Pedido order:
                         orders) {
                        if(simulationDate.isAfter(order.getFechaLimite())){
                            switch (type){
                                case 1: {
                                    StaticValues.collapseFlag = true;
                                    StaticValues.comCollapseFlag = true;
                                    System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Colapso Logistico tipo " + type + " a las " + simulationDate);
                                    break;
                                }
                                case 2: {
                                    StaticValues.collapseSimulationFlag = true;
                                    StaticValues.comSimCollapseFlag = true;
                                    System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Colapso Logistico tipo " + type + " a las " + simulationDate);
                                    break;
                                }
                                case 3: {
                                    StaticValues.fullCollapseFlag = true;
                                    StaticValues.comFullCollapseFlag = true;
                                    System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Colapso Logistico tipo " + type + " a las " + simulationDate);
                                    break;
                                }
                            }
                        }
                    }

                    if(StaticValues.collapseFlag && type == 1) break;
                    if(StaticValues.collapseSimulationFlag && type == 2) break;
                    if(StaticValues.fullCollapseFlag && type == 3) break;

                    switch (type){
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
                    }

                    System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Pedidos atendidos para el tipo " + type + "Tiempo de simulacion " + simulationDate);
                    //log.write(LocalDateTime.now(StaticValues.zoneId) + " Pedidos atendidos para el tipo " + type + "Tiempo de simulacion " + simulationDate + '\n');

                } else {
                    switch (type){
                        case 1: {
                            try {
                                ConcurrentValues.freeUpdateVal.acquire();
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
                                ConcurrentValues.updateValCollapse.release();
                            }catch (Exception e){
                                System.out.println(e.getMessage());
                                ConcurrentValues.updateValCollapse.release();
                            }
                            break;
                        }
                    }
                    System.out.println("No hubieron pedidos para el tipo " + type + " Tiempo de simulacion " + simulationDate);
                    //log.write("No hubieron pedidos para el tipo " + type + " Tiempo de simulacion " + simulationDate + '\n');
                }
                this.simulationDate = (type ==1) ? LocalDateTime.now(StaticValues.zoneId) : this.simulationDate.plusSeconds(sleepTime / 1000 * multiplier);
                //this.startDate = this.startDate.plusSeconds(sleepTime/1000);
                this.startDate = LocalDateTime.now(StaticValues.zoneId);
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
                        switch (type){
                            case 2: {
                                ConcurrentValues.newSimulationDay.release();
                                break;
                            }
                            case 3: {
                                ConcurrentValues.newCollapseDay.release();
                                break;
                            }
                        }
                        nextDay = simulationDate.plusDays(1).truncatedTo(ChronoUnit.DAYS);
                    }
                }

            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Terminando la ejecucion del algoritmo tipo " + type );
        /*try {
            log.write(LocalDateTime.now(StaticValues.zoneId) + " Terminando la ejecucion del algoritmo tipo " + type +'\n');
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //Reiniciamos todo lo utilizado por la simulacion, y lo agregado

        if(type != 1)
            pedidoRepository.terminarSimulacion(type);

    }

}
