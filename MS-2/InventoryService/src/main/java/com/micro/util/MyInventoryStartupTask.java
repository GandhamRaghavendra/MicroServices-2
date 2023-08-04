package com.micro.util;

import com.micro.model.InventoryItem;
import com.micro.repo.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyInventoryStartupTask implements ApplicationRunner {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("Performing startup operations...");

        List<InventoryItem> list = new ArrayList<>();

        list.add(new InventoryItem(1,10));
        list.add(new InventoryItem(2,4));
        list.add(new InventoryItem(3,5));
        list.add(new InventoryItem(4,30));
        list.add(new InventoryItem(5,15));

        inventoryRepo.saveAll(list);

        System.out.println("Insertion Done..!");

    }
}
