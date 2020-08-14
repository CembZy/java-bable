package com.fns.consumers.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.fns.consumers.entity.User;

@FeignClient("usermanager")
public interface ConsumersFeign {

    @GetMapping("get/{id}")
    User get(@PathVariable("id") Long id);//feign的一个坑，@PathVariable注解后面必须指定参数名。
    @GetMapping("get2/{id}")
    User get2(@PathVariable("id") Long id);//feign的一个坑，@PathVariable注解后面必须指定参数名。
}
