package com.stage.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
//@SpringBootApplication(scanBasePackages = "com.stage.spring")
public class ProjetStageApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetStageApplication.class, args);
	}

}
