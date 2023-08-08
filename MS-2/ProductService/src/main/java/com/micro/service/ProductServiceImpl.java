package com.micro.service;

import com.micro.model.Product;
import com.micro.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepo productRepo;


    @Override
    public Optional<Product> getById(Integer id) {
        return productRepo.findById(id);
    }

    @Override
    public List<Product> getAll() {
        return productRepo.findAll();
    }
}
