package com.fns.earuka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
//开启euerka服务端
@EnableEurekaServer
public class EarukaApplication {

	public static void main(String[] args) {
		 SpringApplication.run(EarukaApplication.class, args);
	}
}
