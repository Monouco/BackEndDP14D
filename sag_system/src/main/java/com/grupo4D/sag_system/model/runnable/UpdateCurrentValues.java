package com.grupo4D.sag_system.model.runnable;

import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.CamionRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Component
@Scope("prototype")
public class UpdateCurrentValues implements Runnable{

    @Autowired
    CamionRepository camionRepository;

    private int type;
    private LocalDateTime simulationDate;
    private int multiplier;
    private LocalDateTime endDate;

    @Override
    public void run(){

        this.multiplier = StaticValues.mult;
        this.simulationDate = StaticValues.virtualDate;
        this.endDate = StaticValues.end;
        this.type = StaticValues.simulationType;

        //Considerando 5 min
        long sleepTime = 300000;
        if (endDate != null) this.endDate = this.endDate.plusSeconds(sleepTime/1000*(multiplier + 2));
        try{
            //
            while (true) {

                System.out.println(LocalDateTime.now() + " Actualizando valores para el tipo " + type);
                camionRepository.updatingValues(simulationDate, type);

                if(endDate != null && simulationDate.isAfter(endDate)){
                    break;
                }

                simulationDate = simulationDate.plusSeconds(sleepTime/1000*multiplier);

                Thread.sleep(sleepTime);
            }
        }
        catch (InterruptedException e ){
            System.out.println(e.getMessage());
        }

        System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Terminando el proceso de actualizar para el tipo "+ type);

    }

}
