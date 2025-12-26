package com.transit.transiteye;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TransitEyeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransitEyeApplication.class, args);
	}

}
