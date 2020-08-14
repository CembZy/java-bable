package cn.enjoy.order.controller;

import cn.enjoy.order.pojo.Order;
import cn.enjoy.order.pojo.Product;
import cn.enjoy.order.utils.LoadBalance;
import cn.enjoy.order.utils.RamdomLoadBalance;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by VULCAN on 2018/7/25.
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private RestTemplate restTemplate;


    private LoadBalance loadBalance = new RamdomLoadBalance();

    @RequestMapping("/getOrder/{id}")
    public Object getProduct(HttpServletRequest request, @PathVariable("id") String id) {
        String host = loadBalance.choseServiceHost();
        Product product = restTemplate.getForObject("http://"+host+"/product/getProduct/1", Product.class);
        return new Order(id,"ordername",product);
    }
}
