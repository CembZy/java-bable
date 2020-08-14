package cn.enjoy.controller;

import cn.enjoy.service.IProductClientService;
import cn.enjoy.service.IZUUlClientService;
import cn.enjoy.vo.Product;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/consumer")
public class ConsumerProductController {

    @Resource
    private IProductClientService iProductClientService;


    @Resource
    private IZUUlClientService izuUlClientService;


    @Resource
    private LoadBalancerClient loadBalancerClient;

    @RequestMapping("/product/get")
    public Object getProduct(long id) {
        return  iProductClientService.getProduct(id);
    }

    @RequestMapping("/product/list")
    public  Object listProduct() {
        return iProductClientService.listProduct();
    }

    @RequestMapping("/product/add")
    public Object addPorduct(Product product) {
        return  iProductClientService.addPorduct(product);
    }

    @RequestMapping("/product/getProductAndUser")
    public Object getProductAndUser(long id) {
        Map<String,Object> result = new HashMap();
        result.put("product",izuUlClientService.getProduct(id));
        result.put("user",izuUlClientService.getUsers(id+""));
        return  result;
    }

}