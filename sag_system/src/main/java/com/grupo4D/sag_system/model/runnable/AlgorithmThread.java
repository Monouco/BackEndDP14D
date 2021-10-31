package com.grupo4D.sag_system.model.runnable;

import com.grupo4D.sag_system.SagSystemApplication;
import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.PedidoRepository;
import com.grupo4D.sag_system.service.AlgorithmService;
import com.grupo4D.sag_system.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
    private int type;
    private long offset;

    @Autowired
    AlgorithmService algorithmService;

    @Autowired
    PedidoRepository pedidoRepository;
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
        StaticValues.end = null;

        System.out.println(multiplier);
        //Considerando 10 min
        //long sleepTime = 600000;
        long sleepTime = 180000;
        ArrayList<Pedido> orders;
        try{
            //
            while (true) {
                //Colapso logistico va aqui

                //System.out.println(multiplier + 2);
                if(endDate != null && simulationDate.isAfter(endDate)){
                    StaticValues.endSimulationFlag = true;
                    break;
                }

                orders = pedidoRepository.listarPedidosDisponibles(simulationDate, type);
                if (!orders.isEmpty()) {
                    algorithmService.asignarPedidos(simulationDate, orders, type, offset);
                    System.out.println(LocalDateTime.now() + " Pedidos atendidos para el tipo " + type);
                } else {
                    System.out.println("No hubieron pedidos para el tipo " + type + " Tiempo de simulacion " + simulationDate);
                }
                this.simulationDate = this.simulationDate.plusSeconds(sleepTime / 1000 * multiplier);
                Thread.sleep(sleepTime);
            }
        }
        catch (InterruptedException e ){
            System.out.println(e.getMessage());
        }

    }

}
