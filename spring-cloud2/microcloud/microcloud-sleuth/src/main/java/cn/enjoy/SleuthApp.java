package cn.enjoy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class SleuthApp {
    public static void main(String[] args) {
        SpringApplication.run(SleuthApp.class,args);
    }
}