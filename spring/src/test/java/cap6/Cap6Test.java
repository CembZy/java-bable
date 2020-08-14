package cap6;

import cap6.aop.Calculator;
import cap6.config.Cap10MainConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Cap6Test {
    @Test
    public void test01() {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(Cap10MainConfig.class);

        Calculator c = app.getBean(Calculator.class);
        int result = c.div(4, 2);
        System.out.println(result);
        app.close();
        
    }
}
