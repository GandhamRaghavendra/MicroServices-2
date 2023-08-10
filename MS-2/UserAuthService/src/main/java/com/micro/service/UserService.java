package com.micro.service;

import com.micro.entity.UserData;
import com.micro.repo.AuthRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    String saveUser(UserData userData);

    List<UserData> getAll();

    UserData getUserById(Integer uId);


    String generateJWT(Authentication authentication);

    boolean validateJWT(String token);
}
