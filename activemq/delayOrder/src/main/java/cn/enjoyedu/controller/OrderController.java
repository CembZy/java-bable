package cn.enjoyedu.controller;

import cn.enjoyedu.service.busi.SaveOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 *类说明：用户注册的控制器
 */
@Controller
public class OrderController {
    private static final String SUCCESS = "suc";
    private static final String FAILUER = "failure";

    @Autowired
    private SaveOrder saveOrder;

    @RequestMapping("/index")
    public String userReg(){
        return "order";
    }

    @RequestMapping("/submitOrder")
    @ResponseBody
    public String saveUser(@RequestParam("orderNumber")int orderNumber){
    	saveOrder.insertOrders(orderNumber);
    	return SUCCESS;
    }


}
