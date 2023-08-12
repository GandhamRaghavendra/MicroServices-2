package com.micro.service;

import com.micro.model.InventoryItem;
import com.micro.model.Order;
import com.micro.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public Order placeOrder(Integer pid, Integer qun){

        logger.info("Inside Service Method (ORDER_SERVICE)..!");

        // this is for checking product.
        String url1 = "http://localhost:8082/products/"+pid;

        // this is for getting inventory details.
        String url2 = "http://localhost:8081/inventory/"+pid;

        // this is for checking order is possible of not.
        String url3 = "http://localhost:8081/inventory/"+pid+"/"+qun;


        Product product = restTemplate.exchange(url1, HttpMethod.GET, null, new ParameterizedTypeReference<Product>() {
        }).getBody();

//        System.out.println(product);

        InventoryItem inventoryItem = restTemplate.exchange(url2, HttpMethod.GET, null, new ParameterizedTypeReference<InventoryItem>() {
        }).getBody();

//        System.out.println(inventoryItem);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url3, HttpMethod.POST, null, String.class);
        String res = responseEntity.getBody();

        System.out.println(res);

        if(res.equals("Done")){


            Order order = new Order();

            order.setQuantity(qun);
            order.setProductId(product.getProductId());
            order.setTotalPrice(qun * product.getPrice());
            order.setPName(product.getName());

            return order;
        }

        logger.info("Service Method End (ORDER_SERVICE)..!");

        throw new RuntimeException("Insufficient Qun..!");
    }
}
