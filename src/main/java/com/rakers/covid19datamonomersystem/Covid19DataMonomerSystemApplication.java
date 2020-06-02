package com.rakers.covid19datamonomersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Covid19DataMonomerSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(Covid19DataMonomerSystemApplication.class, args);
	}

}
