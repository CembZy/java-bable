package cn.enjoy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ConfigClientBusApp {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientBusApp.class,args);
    }
}