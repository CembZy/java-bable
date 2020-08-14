package com.fns.consumers.fegin;

import com.fns.consumers.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("usermanager")
public interface ConsumersFeign2 {

    @GetMapping("get2/{id}")
    User get2(@PathVariable("id") Long id);//feign的一个坑，@PathVariable注解后面必须指定参数名。
}
