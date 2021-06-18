package com.m8.event.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@ComponentScan(basePackageClasses = {Application.class, Jsr310JpaConverters.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


}
