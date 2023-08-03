package com.micro.controller;

import com.micro.model.InventoryItem;
import com.micro.repo.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inv")
public class InventoryController {

    @Autowired
    private InventoryRepo inventoryRepo;

    @GetMapping("{id}")
    public ResponseEntity<InventoryItem> getById(@PathVariable int id){

        InventoryItem item =  inventoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Invalid Id..!"));

        return new ResponseEntity<>(item, HttpStatusCode.valueOf(200));
    }
}
