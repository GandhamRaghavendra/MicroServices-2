package com.micro.controller;

import com.micro.model.InventoryItem;
import com.micro.repo.InventoryRepo;
import jdk.dynalink.linker.LinkerServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepo inventoryRepo;

    private final Logger logger =  LoggerFactory.getLogger(InventoryController.class);

    @GetMapping
    public ResponseEntity<List<InventoryItem>> getAllInventoryDetails(){

        List<InventoryItem> all = inventoryRepo.findAll();

        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getById(@PathVariable int id){

        MDC.put("userId","unique_User_Id");

        logger.info("Inside Inventory Controller Method..!");

        MDC.remove("userId");

        InventoryItem item =  inventoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Invalid Id..!"));

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PostMapping("/{pid}/{qun}")
    public ResponseEntity<String> check(@PathVariable Integer pid,@PathVariable Integer qun){

        MDC.put("userId","unique_User_Id");

        logger.info("Inside Inventory Controller Method..!");

        MDC.remove("userId");

        InventoryItem item =  inventoryRepo.findById(pid).orElseThrow(() -> new RuntimeException("Invalid Id..!"));

        if(item.getQuantity() <  qun) throw new RuntimeException("Insufficient Qun");

        item.setQuantity(item.getQuantity() - qun);

        inventoryRepo.save(item);

        return new ResponseEntity<>("Done", HttpStatus.OK);
    }
}
