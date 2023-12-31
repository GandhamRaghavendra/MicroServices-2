package com.micro.service;

import com.micro.entity.UserData;
import com.micro.repo.AuthRequest;
import com.micro.repo.UserRepo;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

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
    public String generateJWT(Authentication authentication, String limit, String apiKey) {
        return jwtService.generateToken(authentication,apiKey,limit);
    }

    @Override
    public void validateJWT(String token) {
        jwtService.validateToken(token);
    }
}
