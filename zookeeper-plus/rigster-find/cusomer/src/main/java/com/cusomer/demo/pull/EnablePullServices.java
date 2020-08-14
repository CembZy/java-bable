package com.cusomer.demo.pull;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PullServices.class)
public @interface EnablePullServices {
}
