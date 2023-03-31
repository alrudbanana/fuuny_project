package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@EnableJpaAuditing
@SpringBootApplication
public class FunnyProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunnyProjectApplication.class, args);
	}

}
