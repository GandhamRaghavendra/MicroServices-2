package com.micro.controller;

import com.micro.entity.UserData;
import com.micro.repo.AuthRequest;
import com.micro.service.UserService;
import jakarta.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auths")
public class UserDataController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserData user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userService.saveUser(user);
    }

    @PostMapping("/token")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPass()));
        if (authenticate.isAuthenticated()) {
            String jwt = userService.generateJWT(authRequest.getUsername());

            return new ResponseEntity<>(jwt,HttpStatus.OK);

        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @PostMapping("/valid")
    public ResponseEntity<String> validateJWT(@PathParam("token") String token){

        if(userService.validateJWT(token)){
            return new ResponseEntity<>("T", HttpStatus.OK);
        }
        return new ResponseEntity<>("F",HttpStatus.FORBIDDEN);
    }

    @GetMapping("/all/users")
    public ResponseEntity<List<UserData>> getAllUsers(){

        List<UserData> all = userService.getAll();

        return new ResponseEntity<>(all,HttpStatus.OK);
    }

}
