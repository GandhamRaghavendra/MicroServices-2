package com.micro.controller;

import com.micro.model.InventoryItem;
import com.micro.model.Order;
import com.micro.model.Product;
import com.micro.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/{pid}/{qun}")
    public ResponseEntity<Order> placeOrder(@PathVariable("pid") Integer pid, @PathVariable("qun") Integer qun){

        MDC.put("userId","unique_User_Id");

        logger.info("Inside ControllerLayer (ORDER_SERVICE)..!");

        MDC.remove("userId");

        Order order = orderService.placeOrder(pid, qun);

        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @GetMapping("/abc")
    public String abc(){
        MDC.put("userId","unique_User_Id");
        logger.info("Inside ControllerLayer (ORDER_SERVICE)..!");
        MDC.remove("userId");
        return "abcd";
    }
}
