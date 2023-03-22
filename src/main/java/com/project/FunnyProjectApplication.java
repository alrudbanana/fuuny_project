package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@EnableJpaAuditing
@SpringBootApplication
public class FunnyProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunnyProjectApplication.class, args);
	}

}
