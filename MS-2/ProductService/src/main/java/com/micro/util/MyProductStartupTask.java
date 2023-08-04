package com.micro.util;

import com.micro.model.Product;
import com.micro.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyProductStartupTask implements ApplicationRunner {

    @Autowired
    private ProductRepo productRepol;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("Performing startup operations...");

        List<Product> list = new ArrayList<>();

        list.add(new Product("pen","Best In Class",20));
        list.add(new Product("pencil","Best In Class",5));
        list.add(new Product("noteBook","Best In Class",30));
        list.add(new Product("phone","Best In Class",15000));
        list.add(new Product("TWS","Best In Class",2000));

        productRepol.saveAll(list);

//        System.out.println(productRepol.findAll());

        System.out.println("Insertion Done..!");

    }
}
