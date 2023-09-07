package com.micro.service;

import com.micro.model.InventoryItem;
import com.micro.model.Order;
import com.micro.model.Product;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

//    public Order placeOrder(Integer pid, Integer qun){
//
//        MDC.put("userId","unique_User_Id");
//
//        logger.info("Inside Service Method (ORDER_SERVICE)..!");
//
//        MDC.remove("userId");
//
//        // this is for checking product.
//        String url1 = "http://localhost:8082/products/"+pid;
//
//        // this is for getting inventory details.
//        String url2 = "http://localhost:8081/inventory/"+pid;
//
//        // this is for checking order is possible of not.
//        String url3 = "http://localhost:8081/inventory/"+pid+"/"+qun;
//
//
//        Product product = restTemplate.exchange(url1, HttpMethod.GET, null, new ParameterizedTypeReference<Product>() {
//        }).getBody();
//
//        InventoryItem inventoryItem = restTemplate.exchange(url2, HttpMethod.GET, null, new ParameterizedTypeReference<InventoryItem>() {
//        }).getBody();
//
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url3, HttpMethod.POST, null, String.class);
//        String res = responseEntity.getBody();
//
//        System.out.println(res);
//
//        if(res.equals("Done")){
//
//
//            Order order = new Order();
//
//            order.setQuantity(qun);
//            order.setProductId(product.getProductId());
//            order.setTotalPrice(qun * product.getPrice());
//            order.setPName(product.getName());
//
//            return order;
//        }
//
//        MDC.put("userId","unique_User_Id");
//
//        logger.info("Service Method End (ORDER_SERVICE)..!");
//
//        MDC.remove("userId");
//
//        throw new RuntimeException("Insufficient Qun..!");
//    }

    public Order newPlaceOrder(Integer pId, Integer qun){

        List<Product> products = productService.getAllProducts();

        List<InventoryItem> inventoryItems = inventoryService.getAllInventoryData();

        Product product = products.stream()
                .filter(p -> p.getProductId() == pId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not exist with this id: "+pId));

        inventoryService.updateInventoryCache(inventoryItems, pId, qun);

        Order order = new Order();

        order.setProductId(product.getProductId());
        order.setPName(product.getName());
        order.setQuantity(qun);
        order.setTotalPrice(product.getPrice() * qun);

        return order;
    }

}
