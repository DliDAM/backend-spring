package com.dlidam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
public class DlidamApplication {

	public static void main(String[] args) {
		SpringApplication.run(DlidamApplication.class, args);
	}

}
