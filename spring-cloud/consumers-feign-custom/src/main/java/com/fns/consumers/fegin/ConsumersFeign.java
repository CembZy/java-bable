package com.fns.consumers.fegin;

import config.FeignConfiguration;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import com.fns.consumers.entity.User;

//使用自定义的fegin，FeignConfiguration
@FeignClient(name = "usermanager", configuration = FeignConfiguration.class)
public interface ConsumersFeign {

    //    @GetMapping("get/{id}") 因为使用的是fegin默认的契约，所以springmvc的注解不支持了
    //    User get(@PathVariable("id") Long id);//feign的一个坑，@PathVariable注解后面必须指定参数名。

    //请求方式必须大写
    @RequestLine("GET get/{id}")
    User get(@Param("id") Long id);
}
