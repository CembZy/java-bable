package cap4.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class Train implements InitializingBean, DisposableBean {

    public Train() {
        System.out.println("Train......constructor............");
    }

    //在bean销毁之前调用这个方法
    @Override
    public void destroy() throws Exception {
        System.out.println("Train......destory......");
        //logger.error
    }

    //在bean属性赋值和初始化之后调用这个方法
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Train.......afterPropertiesSet()...");

    }

}
