package config;


import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//注意，这个ribbon组件，必须在springboot启动类外层目录
@Configuration
public class RibbonConfiguration {

    @Autowired
    private IClientConfig iClientConfig;

    @Bean
    public IRule ribbonRule(IClientConfig iClientConfig){
        return new RandomRule();
    }


}
