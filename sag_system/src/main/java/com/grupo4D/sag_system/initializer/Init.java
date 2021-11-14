package com.grupo4D.sag_system.initializer;

import com.grupo4D.sag_system.SagSystemApplication;
import com.grupo4D.sag_system.model.runnable.AlgorithmThread;
import com.grupo4D.sag_system.model.runnable.FillDeposit;
import com.grupo4D.sag_system.model.runnable.UpdateCurrentValues;
import com.grupo4D.sag_system.model.statics.StaticValues;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.FutureTask;

@Component
public class Init implements InitializingBean {

    @Autowired
    private Environment environment;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {

        StaticValues.mult = 1;
        StaticValues.start = LocalDateTime.now(StaticValues.zoneId);
        StaticValues.virtualDate = LocalDateTime.now(StaticValues.zoneId);
        StaticValues.simulationType = 1;
        StaticValues.end = null;

        UpdateCurrentValues updating = applicationContext.getBean(UpdateCurrentValues.class);

        taskExecutor.execute(updating);

        FillDeposit fillDeposit = applicationContext.getBean(FillDeposit.class);

        taskExecutor.execute(fillDeposit);

        //LOG.info(Arrays.asList(environment.getDefaultProfiles()));
        AlgorithmThread algorithm = applicationContext.getBean(AlgorithmThread.class);

        taskExecutor.execute(algorithm);


    }

}
