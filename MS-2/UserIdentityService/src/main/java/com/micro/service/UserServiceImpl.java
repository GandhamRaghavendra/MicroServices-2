package com.micro.service;

import com.micro.entity.UserData;
import com.micro.repo.AuthRequest;
import com.micro.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Override
    public String saveUser(UserData userData) {
        userRepo.save(userData);
        return "Done..!";
    }

    @Override
    public List<UserData> getAll() {
        return userRepo.findAll();
    }

    @Override
    public UserData getUserById(Integer uId) {
       return userRepo.findById(uId).orElseThrow(()-> new RuntimeException("Invalid uId"));
    }

    @Override
    public String generateJWT(AuthRequest authRequest) {

        return null;
    }

    @Override
    public boolean validateJWT(String token) {

        return false;
    }
}
