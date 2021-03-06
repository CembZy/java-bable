package com.cemb.ssm.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: CemBService
 * @Auther: CemB
 * @Description: RequestParam注解实现
 * @Email: 729943791@qq.com
 * @Date: 2018/7/10 10:43
 */

@Target({ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CemBRequestParam {

    //参数名
    String value() default "";
}
