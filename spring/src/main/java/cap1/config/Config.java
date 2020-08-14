package cap1.config;

import cap1.model.User;
import org.springframework.context.annotation.*;

//配置类===配置文件
@Configuration

//扫描bean,value是扫描路径
//如果要配置过滤扫描条件，就要将useDefaultFilters默认的扫描条件配置成false
//includeFilters是仅扫描某个路径下的文件，其中@Filter是过滤条件，可以配置多个。使用excludeFilters的时候
//要将useDefaultFilters设置为true才能生效
@ComponentScan(value = "cap1"
//        useDefaultFilters = false,
//        includeFilters = {
////                @Filter(type = FilterType.ANNOTATION, classes = Controller.class)
//
//                //自定义过滤类
//                @Filter(type = FilterType.CUSTOM, classes = CustomFilter.class)
//        }
)
public class Config {

    // 注解方式加载bean
    @Bean("user")

    //@Scope配置bean的创建是单例还是多例
    /**
     * 多例：在调用bean的时候才会被创建
     * 单例：在创建IOC容器的时候就会创建bean
     */
//    @Scope("prototype")

    //懒加载：主要针对单例bean，在IOC容器加载的时候不创建bean，只有在调用的时候才创建bean
    @Lazy
    public User user01() {
        return new User();
    }
}
