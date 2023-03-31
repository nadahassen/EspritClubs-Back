package com.stage.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
public class ProjetStageApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetStageApplication.class, args);
	}

}
