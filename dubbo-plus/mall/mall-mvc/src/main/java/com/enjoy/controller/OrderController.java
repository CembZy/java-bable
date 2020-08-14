package com.enjoy.controller;

import com.enjoy.entity.OrderEntiry;
import com.enjoy.service.OrderService;
import com.enjoy.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PayService payService;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String getDetail(HttpServletRequest request, HttpServletResponse response){
        OrderEntiry orderView = orderService.getDetail("1");

        request.setAttribute("order", orderView);
        return "order";
    }

    @RequestMapping(value = "/order/submit", method = RequestMethod.GET)
    public String submit(HttpServletRequest request, HttpServletResponse response){
        OrderEntiry orderView = orderService.getDetail("1");
        orderView = orderService.submit(orderView);

        request.setAttribute("order", orderView);
        return "../order";
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    public String cancel(HttpServletRequest request, HttpServletResponse response){
        OrderEntiry orderView = orderService.getDetail("1");

        boolean cancelOrder = orderService.cancel(orderView);
        boolean cancelpay = payService.cancelPay(orderView.getMoney());

        request.setAttribute("cancelOrder", cancelOrder);
        request.setAttribute("cancelpay", cancelpay);
        return "/cancel";
    }

}
