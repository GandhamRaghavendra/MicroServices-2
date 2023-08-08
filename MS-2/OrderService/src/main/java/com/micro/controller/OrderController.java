package com.micro.controller;

import com.micro.model.InventoryItem;
import com.micro.model.Order;
import com.micro.model.Product;
import com.micro.service.OrderService;
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

    @PostMapping("/{pid}/{qun}")
    public ResponseEntity<Order> placeOrder(@PathVariable("pid") Integer pid, @PathVariable("qun") Integer qun){

        Order order = orderService.placeOrder(pid, qun);

        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @GetMapping("/abc")
    public String abc(){
        return "abcd";
    }
}