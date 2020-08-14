package cap8;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Cap8Test {
    @Test
    public void test01() {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(Cap12MainConfig.class);

        app.close();
        
    }
}
