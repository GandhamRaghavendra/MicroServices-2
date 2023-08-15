package com.micro.util;

import com.micro.model.Product;
import com.micro.repo.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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

    private final Logger logger = LoggerFactory.getLogger(MyProductStartupTask.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {

        MDC.put("userId","unique_User_Id");

        logger.info("Performing startup operations (PRODUCT_SERVICE)...");

        List<Product> list = new ArrayList<>();

        list.add(new Product("pen","Best In Class",20));
        list.add(new Product("pencil","Best In Class",5));
        list.add(new Product("noteBook","Best In Class",30));
        list.add(new Product("phone","Best In Class",15000));
        list.add(new Product("TWS","Best In Class",2000));

        if(productRepol.findById(1).isEmpty()){
            productRepol.saveAll(list);
        }
        else {
            logger.info("Data Already Present in DB..");
        }

        if(productRepol.findAll().isEmpty()) logger.warn("Product Insertion Failed..!");

        logger.info("Start Method Ended (PRODUCT_SERVICE)..!");

        MDC.remove("userId");
    }
}
