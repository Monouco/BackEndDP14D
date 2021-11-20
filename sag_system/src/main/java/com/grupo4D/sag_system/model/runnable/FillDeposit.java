package com.grupo4D.sag_system.model.runnable;

import com.grupo4D.sag_system.model.statics.ConcurrentValues;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.PlantaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@Scope("prototype")
public class FillDeposit implements Runnable{

    private int multiplier;
    private LocalDateTime startDate;
    private int type;

    @Autowired
    PlantaRepository plantaRepository;

    @Override
    public void run(){

        startDate = StaticValues.start;
        this.multiplier = StaticValues.mult;
        this.type = StaticValues.simulationType;

        long sleepTime = (long)((double)ChronoUnit.MILLIS.between(startDate, startDate.plusDays(1).truncatedTo(ChronoUnit.DAYS))/multiplier);
        try {
            while (true) {
                if(type != 1){
                    switch (type){
                        case 2: {
                            ConcurrentValues.newSimulationDay.acquire();
                            if(StaticValues.comEndSimFlag)
                                return;
                            System.out.println("Nuevo dia de simulacion "+ type + " ####");
                            break;
                        }
                        case 3: {
                            ConcurrentValues.newCollapseDay.acquire();
                            if(StaticValues.comEndCollapseFlag)
                                return;
                            System.out.println("Nuevo dia de simulacion "+ type + " ####");
                            break;
                        }
                    }
                }
                else{
                    try {
                        Thread.sleep(sleepTime);
                    }
                    catch (InterruptedException e){
                        System.out.println(e.getMessage());
                    }
                }
                plantaRepository.fillDeposit(type);
                sleepTime = (long)((double)3600000 * 24 / multiplier);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
