package com.fns.producters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProductersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductersApplication.class, args);
	}
}
