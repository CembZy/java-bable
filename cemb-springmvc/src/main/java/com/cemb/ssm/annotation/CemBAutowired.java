package com.cemb.ssm.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: CemBService
 * @Auther: CemB
 * @Description: Autowired注解
 * @Email: 729943791@qq.com
 * @Date: 2018/7/10 10:43
 */

@Target({ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CemBAutowired {

    //是否根据被注入的实例的类型查找
    boolean required() default true;
}
