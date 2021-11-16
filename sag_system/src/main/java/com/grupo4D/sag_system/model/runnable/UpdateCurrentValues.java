package com.grupo4D.sag_system.model.runnable;

import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.statics.ConcurrentValues;
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
        long sleepTime ;
        if(type == 1){
            sleepTime = 180000;
        }
        else{
            sleepTime = 3000;
        }
        if (endDate != null) this.endDate = this.endDate.plusSeconds(sleepTime/1000*(multiplier + 2));
        try{
            //
            while (true) {

                System.out.println(LocalDateTime.now() + " Actualizando valores para el tipo " + type);
                camionRepository.updatingValues(simulationDate, type);

                if(endDate != null && simulationDate.isAfter(endDate)){
                    break;
                }

                if(StaticValues.comCollapseFlag && type == 1) {
                    StaticValues.comCollapseFlag = false;
                    break;
                }

                if(StaticValues.comSimCollapseFlag && type == 2) {
                    StaticValues.comSimCollapseFlag = false;
                    break;
                }

                if(StaticValues.comFullCollapseFlag && type == 3) {
                    StaticValues.comFullCollapseFlag = false;
                    break;
                }


                switch (type){
                    case 1: {
                        try {
                            ConcurrentValues.freeUpdateVal.release();
                            ConcurrentValues.updateVal.acquire();
                            simulationDate = simulationDate.plusSeconds(sleepTime/1000*multiplier);
                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                    case 2: {
                        try {
                            ConcurrentValues.freeUpdateValSimulation.release();
                            ConcurrentValues.updateValSimulation.acquire();
                            simulationDate = simulationDate.plusSeconds(sleepTime/1000*multiplier);
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                    case 3: {
                        try {
                            ConcurrentValues.freeUpdateValCollapse.release();
                            ConcurrentValues.updateValCollapse.acquire();
                            simulationDate = simulationDate.plusSeconds(sleepTime/1000*multiplier);
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                }


                Thread.sleep(sleepTime);
            }
        }
        catch (InterruptedException e ){
            System.out.println(e.getMessage());
        }

        System.out.println(LocalDateTime.now(StaticValues.zoneId) + " Terminando el proceso de actualizar para el tipo "+ type);

    }

}
