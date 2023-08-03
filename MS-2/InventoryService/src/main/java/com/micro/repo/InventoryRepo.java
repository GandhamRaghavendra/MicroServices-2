package com.micro.repo;

import com.micro.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository<InventoryItem,Integer> {
}
