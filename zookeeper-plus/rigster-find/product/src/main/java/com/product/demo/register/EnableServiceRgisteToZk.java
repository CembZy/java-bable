package com.product.demo.register;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RgisteToZk.class)
public @interface EnableServiceRgisteToZk {

}
