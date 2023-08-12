package com.micro.service;

import com.micro.controller.ProductController;
import com.micro.model.Product;
import com.micro.repo.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepo productRepo;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public Optional<Product> getById(Integer id) {

        logger.info("Inside Service Method (PRODUCT_SERVICE)..!");

        return productRepo.findById(id);
    }

    @Override
    public List<Product> getAll() {

        logger.info("Inside Service Method (PRODUCT_SERVICE)..!");

        return productRepo.findAll();
    }
}
