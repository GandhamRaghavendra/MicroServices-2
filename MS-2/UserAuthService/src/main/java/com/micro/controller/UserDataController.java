package com.micro.controller;

import com.micro.dto.UserRegisterRequest;
import com.micro.entity.UserData;
import com.micro.repo.UserRepo;
import com.micro.service.UserService;
import com.micro.service.UuidService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auths")
public class UserDataController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UuidService uuid;

    @Autowired
    private UserRepo userRepo;

    private final Logger logger = LoggerFactory.getLogger(UserDataController.class);

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserRegisterRequest user) {

        MDC.put("userId","unique_User_Id");

        logger.info("Inside Controller Layer (/register)..!");

        MDC.remove("userId");

       UserData userData =  new UserData();

       userData.setPassword(passwordEncoder.encode(user.getPass()));
       userData.setName(user.getName());
       userData.setEmail(user.getMail());
       userData.getRoles().add(user.getRole());

       userService.saveUser(userData);

       return "Done";
    }

    @PostMapping("/token")
    public ResponseEntity<String> generateToken(){

        // creating apiKey.
        String apiKey = uuid.generateAndReturnUUID();

        MDC.put("userId","unique_User_Id");

        logger.info("Inside Controller Layer (/token)..!");

        MDC.remove("userId");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Everytime storing new apiKey to the user.
        UserData userData = userRepo.findByName(authentication.getName()).orElseThrow(() -> new BadRequestException("Invalid userName..!"));

        userData.setKey(apiKey);

        userRepo.save(userData);
        // End.

        String jwt = userService.generateJWT(authentication, userData.getLimit().toString(), userData.getKey());

        return new ResponseEntity<>(jwt,HttpStatus.OK);
    }

    @PostMapping("/valid")
    public ResponseEntity<String> validateJWT(@PathParam("token") String token){

        MDC.put("userId","unique_User_Id");

        logger.info("Inside Controller Layer (/valid)..!");

        MDC.remove("userId");

        return new ResponseEntity<>("Validation Done..!", HttpStatus.OK);
    }

    @GetMapping("/all/users")
    public ResponseEntity<List<UserData>> getAllUsers(){

        //  todo: this point only accessed by admin..

        MDC.put("userId","unique_User_Id");

        logger.info("Inside Controller Layer (/all/users)..!");

        MDC.remove("userId");

        List<UserData> all = userService.getAll();

        return new ResponseEntity<>(all,HttpStatus.OK);
    }

    @GetMapping("/admin")
    public String adminaccess(){
        return "Only Admin..!";
    }

}
