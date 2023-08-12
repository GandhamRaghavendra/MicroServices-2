package com.micro.service;


import com.micro.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> getById(Integer id);
    List<Product> getAll();
}
