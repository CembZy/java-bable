package cap5.service;

import cap5.dao.TestDao;
import org.springframework.stereotype.Service;

import javax.inject.Inject;


@Service
public class TestService {
    //@Qualifier("testDao")
    //@Autowired(required=false)
    //效果是一样的,与Autowired一样可以装配bean
    //1,不支持Primary功能
    //2,不支持Autowired false
    //@Resource(name="testDao2")
    @Inject //@Inject 是JSR-330标准的注解
    private TestDao testDao;

    public void println() {
        System.out.println(testDao);
    }
}
