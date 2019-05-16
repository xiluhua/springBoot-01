package com.springBoot1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import com.springBoot1.boot.Bootup;

@ImportResource(value = {"classpath:applicationContext.xml"})
@SpringBootApplication
public class SpringBoot01Application {

	public static void main(String[] args) {
		
		new Bootup().envAndAopConfig(args);
		SpringApplication.run(SpringBoot01Application.class, args);
	}

}

