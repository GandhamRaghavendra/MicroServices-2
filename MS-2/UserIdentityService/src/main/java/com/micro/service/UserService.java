package com.micro.service;

import com.micro.entity.UserData;
import com.micro.repo.AuthRequest;

import java.util.List;

public interface UserService {

    String saveUser(UserData userData);

    List<UserData> getAll();

    UserData getUserById(Integer uId);


    String generateJWT(AuthRequest authRequest);

    boolean validateJWT(String token);
}
