package com.grupo4D.sag_system.model.runnable;

import com.grupo4D.sag_system.controller.AveriaController;
import com.grupo4D.sag_system.model.request.AveriaFront;
import com.grupo4D.sag_system.model.statics.ConcurrentValues;
import com.grupo4D.sag_system.model.statics.StaticValues;
import jdk.dynalink.beans.StaticClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
@Scope("prototype")
public class AveriaScheduled implements Runnable {

    private int type;
    private LocalDateTime simulationDate;
    private int multiplier;
    private LocalDateTime startDate;
    private int idCamion;

    @Autowired
    AveriaController averiaController;

    @Override
    public void run(){
        this.type = StaticValues.simulationType;
        this.simulationDate = StaticValues.virtualDate;
        this.startDate = StaticValues.start;
        this.multiplier = StaticValues.mult;
        // TODO: Revisar este caso por problemas de concurrencia
        this.idCamion = StaticValues.idCamion;
        long sleepTime = ChronoUnit.HOURS.between(simulationDate,startDate) * 3600000 / multiplier;

        try{
            ConcurrentValues.allowFail.acquire();
            System.out.println("Averia programada para el camion " + idCamion + " a la hora " + startDate);
            Thread.sleep(sleepTime);
            AveriaFront averiaFront = new AveriaFront();
            averiaFront.setType(type);
            averiaFront.setFecha(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss")));
            averiaFront.setIdCamion(idCamion);
            averiaFront.setMultiplier(multiplier);
            averiaController.registrarAveriaNueva(averiaFront);
        }
        catch (InterruptedException e){
            System.out.println(e.getMessage());
        }

    }
}
