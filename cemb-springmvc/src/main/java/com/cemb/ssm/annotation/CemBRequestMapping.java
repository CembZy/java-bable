package com.cemb.ssm.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: CemBService
 * @Auther: CemB
 * @Description: RequestMapping注解实现
 * @Email: 729943791@qq.com
 * @Date: 2018/7/10 10:43
 */

@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CemBRequestMapping {

    //定义路径参数
    String value()default "";
}
