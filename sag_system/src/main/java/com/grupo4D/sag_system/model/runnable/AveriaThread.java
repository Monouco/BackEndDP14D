package com.grupo4D.sag_system.model.runnable;

import com.grupo4D.sag_system.model.Camion;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Scope("prototype")
public class AveriaThread implements Runnable{

    @Autowired
    CamionRepository camionRepository;

    private int type;
    private int idCamion;
    private int multiplier;
    private Camion camion;

    @Override
    public void run(){

        this.type = StaticValues.simulationType;
        this.idCamion = StaticValues.idCamion;
        this.multiplier = StaticValues.mult;
        //this.startDate = StaticValues.start;
        long sleepTime = (long)((double)3600000 / this.multiplier);
        try{
            System.out.println("Empezando averia");
            //Esperando el tiempo para generar el mantenimiento correctivo
            Thread.sleep(sleepTime);
            camion = camionRepository.findCamionById(idCamion);
            switch (type){
                case 1: {
                    camion.setEstado("Mantenimiento Correctivo");
                    break;
                }

                case 2: {
                    camion.setEstadoSimulacion("Mantenimiento Correctivo");
                    break;
                }

                case 3: {
                    camion.setEstadoColapso("Mantenimiento Correctivo");
                    break;
                }
            }
            camionRepository.save(camion);
        }
        catch (InterruptedException e ){
            System.out.println(e.getMessage());
        }

    }

}
