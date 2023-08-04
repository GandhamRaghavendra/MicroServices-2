package com.micro.controller;

import com.micro.model.Product;
import com.micro.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{Id}")
    public ResponseEntity<Product> getProductById(@PathVariable("Id") Integer id){

       Product product =  productService.getById(id).orElseThrow(() -> new RuntimeException("Invalid ProductID..!"));

       return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
