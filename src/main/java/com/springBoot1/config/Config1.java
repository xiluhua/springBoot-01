package com.springBoot1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springBoot1.bean.Dog;

@Configuration
public class Config1 {

	@Bean
	public Dog dog1() {
		return new Dog();
	}
	
}
