package com.example.api_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
@Configuration
@EntityScan(basePackages = "com.example.api_app.model")
@SpringBootApplication
public class ApiAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiAppApplication.class, args);
	}

}
