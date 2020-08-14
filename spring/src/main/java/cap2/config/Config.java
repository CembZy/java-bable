package cap2.config;

import cap2.model.User;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(value = "cap2")
public class Config {

    @Bean("user1")
    public User user01() {
        return new User();
    }

    @Bean("user2")
    //添加过滤条件
    @Conditional(WinCondition.class)
    public User user02() {
        return new User();
    }

    @Bean("user3")
    @Conditional(LinuxCondition.class)
    public User user03() {
        return new User();
    }
}
