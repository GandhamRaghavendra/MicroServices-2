package com.micro.controller;

import com.micro.model.InventoryItem;
import com.micro.repo.InventoryRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepo inventoryRepo;

    private final Logger logger =  LoggerFactory.getLogger(InventoryController.class);

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getById(@PathVariable int id){

        logger.info("Inside Inventory Controller Method..!");

        InventoryItem item =  inventoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Invalid Id..!"));

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PostMapping("/{pid}/{qun}")
    public ResponseEntity<String> check(@PathVariable Integer pid,@PathVariable Integer qun){

        logger.info("Inside Inventory Controller Method..!");

        InventoryItem item =  inventoryRepo.findById(pid).orElseThrow(() -> new RuntimeException("Invalid Id..!"));

        if(item.getQuantity() <  qun) throw new RuntimeException("Insufficient Qun");

        item.setQuantity(item.getQuantity() - qun);

        inventoryRepo.save(item);

        return new ResponseEntity<>("Done", HttpStatus.OK);
    }
}
