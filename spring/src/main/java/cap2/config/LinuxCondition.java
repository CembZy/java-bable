package cap2.config;


import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

//自定义条件过滤器
public class LinuxCondition implements Condition {

    //过滤规则
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        /**
         * BeanFactory:获取容器中的bean的实例
         * FactoryBean:向容器中添加bean的实例
         */
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        Environment environment = context.getEnvironment();
        //获取系统的名称
        String property = environment.getProperty("os.name");

        if (property.contains("Linux")) {
            return true;
        }
        return false;
    }
}
