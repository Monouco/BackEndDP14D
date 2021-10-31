package com.grupo4D.sag_system;

import com.grupo4D.sag_system.model.runnable.AlgorithmThread;
import com.grupo4D.sag_system.model.statics.StaticValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;

import java.time.LocalDateTime;

@SpringBootApplication
public class SagSystemApplication {




	public static void main(String[] args) {
		SpringApplication.run(SagSystemApplication.class, args);
		//AlgorithmThread algorithm = new AlgorithmThread(1, LocalDateTime.now(), LocalDateTime.now(), 1);
		StaticValues.mult = 1;
		StaticValues.start = LocalDateTime.now();
		StaticValues.virtualDate = LocalDateTime.now();
		StaticValues.simulationType = 1;
		StaticValues.collapseFlag = false;
		StaticValues.collapseSimulationFlag = false;
		StaticValues.fullCollapseFlag = false;
		StaticValues.endSimulationFlag = false;

		//new Thread(algorithm).run();
		/*AlgorithmThread algorithm = applicationContext.getBean(AlgorithmThread.class);

		taskExecutor.execute(algorithm);*/
	}

}
