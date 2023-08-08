package com.micro.controller;

import com.micro.entity.UserData;
import com.micro.repo.AuthRequest;
import com.micro.service.UserService;
import com.micro.service.UserServiceImpl;
import jakarta.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auths")
public class UserDataController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserData user) {
        return userService.saveUser(user);
    }

    @PostMapping("/token")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequest authRequest){

        String jwt = userService.generateJWT(authRequest);

        return new ResponseEntity<>(jwt,HttpStatus.OK);
    }

    @PostMapping("/valid")
    public ResponseEntity<String> validateJWT(@PathParam("token") String token){

        if(userService.validateJWT(token)){
            return new ResponseEntity<>("T", HttpStatus.OK);
        }
        return new ResponseEntity<>("F",HttpStatus.FORBIDDEN);
    }


}
