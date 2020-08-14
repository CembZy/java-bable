package cap5.config;

import cap5.dao.TestDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan({"cap5.controller", "cap5.service", "cap5.dao"})
public class Cap9MainConfig {
    //@Primary 是指定Autowired注入bean的优先级
    @Bean("testDao")
    public TestDao testDao() {
        TestDao testDao = new TestDao();
        testDao.setFlag("2");
        return testDao;
    }
}
