package com.micro.controller;

import com.micro.dto.UserRegisterRequest;
import com.micro.entity.UserData;
import com.micro.repo.AuthRequest;
import com.micro.service.SecurityConstant;
import com.micro.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/auths")
public class UserDataController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(UserDataController.class);

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserRegisterRequest user) {

        logger.info("Inside Controller Layer (/register)..!");

       UserData userData =  new UserData();

       userData.setPassword(passwordEncoder.encode(user.getPass()));
       userData.setName(user.getName());
       userData.setEmail(user.getMail());
       userData.setRole(user.getRole());

       userService.saveUser(userData);

       return "Done";
    }

    @PostMapping("/token")
    public ResponseEntity<String> generateToken(){

        //todo: on execution of this method we should create api key..

        logger.info("Inside Controller Layer (/token)..!");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String jwt = userService.generateJWT(authentication);

        return new ResponseEntity<>(jwt,HttpStatus.OK);
    }

    @PostMapping("/valid")
    public ResponseEntity<String> validateJWT(@PathParam("token") String token){

        logger.info("Inside Controller Layer (/valid)..!");

        if(userService.validateJWT(token)){
            return new ResponseEntity<>("T", HttpStatus.OK);
        }
        return new ResponseEntity<>("F",HttpStatus.FORBIDDEN);
    }

    @GetMapping("/all/users")
    public ResponseEntity<List<UserData>> getAllUsers(){

        // todo: this point only accessed by admin..
        logger.info("Inside Controller Layer (/all/users)..!");

        List<UserData> all = userService.getAll();

        return new ResponseEntity<>(all,HttpStatus.OK);
    }

}
