package com.bqt.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.naming.Context;

//Annotation @EnableAutoConfiguration allow config file properties
//@Component, @Configuration, @Bean can tick @Profile so that limit when it loaded
@SpringBootApplication
public class EcommerceApplication {
	// Life Cycle
	// when IoC Container (ApplicationContext) find out a Bean need to control, it will Init by Constructor
	// inject dependencies into Bean by Setter, and perform processes setting other into Bean as setBeanName, setBeanClassLoader, v.v...
	// method tick @PostConstruct was called
	// pretreatment  after when @PostConstruct was called.
	// Bean is ready as active
	// if IoC Container don't control bean or  shutdown it will call method @PreDestroy in Bean
	// Remove Bean.
	public static void main(String[] args) {
		// ApplicationContext contain all dependence(Bean) in project
		SpringApplication.run(EcommerceApplication.class, args);
	}

}


