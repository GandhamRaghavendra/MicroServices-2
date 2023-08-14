package com.micro.repo;

import com.micro.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserData,Integer> {
    Optional<UserData> findByName(String username);

    Optional<UserData> findByKey(String key);
}
