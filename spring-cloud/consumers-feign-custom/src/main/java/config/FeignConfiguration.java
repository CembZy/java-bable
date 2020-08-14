package config;

import feign.Contract;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//自定义的fegin，FeignConfiguration
@Configuration
public class FeignConfiguration {

    //设置契约，这里设置Default，fegin默认的，所以就不支持springmvc的注解，想要支持，可以使用springmvc的契约
    @Bean
    public Contract feginContract() {
        return new Contract.Default();
    }

    //配置fegin的日志
    @Bean
    public Logger.Level gefinLoggerLevel() {
        return Logger.Level.FULL;
    }

}
