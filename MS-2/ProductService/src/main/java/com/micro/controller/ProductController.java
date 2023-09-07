package com.micro.controller;

import com.micro.model.Product;
import com.micro.service.ProductService;
import jakarta.persistence.PostRemove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){

        logger.info("Inside Controller Method (PRODUCT_SERVICE)..!");

        List<Product> all = productService.getAll();

        return new ResponseEntity<>(all,HttpStatus.OK);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Product> getProductById(@PathVariable("Id") Integer id){

        logger.info("Inside Controller Method (PRODUCT_SERVICE)..!");

       Product product =  productService.getById(id).orElseThrow(() -> new RuntimeException("Invalid ProductID..!"));

       return new ResponseEntity<>(product, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Product> updateProduct(@PathVariable Integer pId,@RequestBody Product product){

        //TODO: implement api key for this method..
        //TODO: one admin can access this method..

        return new ResponseEntity<>(null,HttpStatus.OK);
    }

}
