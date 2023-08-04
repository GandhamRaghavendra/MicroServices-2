package com.micro.service;

import com.micro.model.InventoryItem;
import com.micro.model.Order;
import com.micro.model.Product;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    public Order placeOrder(Integer pid, Integer qun){

        // this is for checking product.
        String url1 = "http://localhost:8082/products/"+pid;

        // this is for getting inventory details.
        String url2 = "http://localhost:8081/inventory/"+pid;

        // this is for checking order is possible of not.
        String url3 = "http://localhost:8081/inventory/"+pid+"/"+qun;


//        System.out.println(modelService.getProduct(pid));


        Product product = restTemplate.exchange(url1, HttpMethod.GET, null, new ParameterizedTypeReference<Product>() {
        }).getBody();

        System.out.println(product);

        InventoryItem inventoryItem = restTemplate.exchange(url2, HttpMethod.GET, null, new ParameterizedTypeReference<InventoryItem>() {
        }).getBody();

        System.out.println(inventoryItem);

        String res = restTemplate.getForObject(url3, String.class);

        System.out.println(res);

//        if(res.equals("Done")){


            Order order = new Order();

            order.setQuantity(qun);
            order.setProductId(product.getProductId());
            order.setTotalPrice(qun * product.getPrice());

            return order;
//        }

//        throw new RuntimeException("Insufficient Qun..!");
    }

}
