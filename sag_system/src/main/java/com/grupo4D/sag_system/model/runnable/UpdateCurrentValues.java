package com.grupo4D.sag_system.model.runnable;

import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Component
@Scope("prototype")
public class UpdateCurrentValues implements Runnable{

    @Autowired
    CamionRepository camionRepository;

    @Override
    public void run(){

        //Considerando 5 min
        long sleepTime = 300000;
        try{
            //
            while (true) {
                camionRepository.updatingValues(LocalDateTime.now().minusHours(5));
                Thread.sleep(sleepTime);
            }
        }
        catch (InterruptedException e ){
            System.out.println(e.getMessage());
        }

    }

}
