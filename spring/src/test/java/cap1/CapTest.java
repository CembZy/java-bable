package cap1;

import cap1.config.Config;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CapTest {

    @Test
    public void mainTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);

    }

}
