package com.cemb.ssm.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: CemBService
 * @Auther: CemB
 * @Description: Service注解实现
 * @Email: 729943791@qq.com
 * @Date: 2018/7/10 10:43
 */

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CemBService {

    //service实现类参数
    String value() default "";
}
