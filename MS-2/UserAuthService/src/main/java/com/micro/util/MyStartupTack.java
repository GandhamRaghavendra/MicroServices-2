package com.micro.util;

import com.micro.entity.RateLimit;
import com.micro.entity.Role;
import com.micro.entity.UserData;
import com.micro.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class MyStartupTack implements ApplicationRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(MyStartupTack.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {

        MDC.put("userId", "unique_User_Id");

        logger.info("Inside Startup Method..!");


        List<UserData> list = new ArrayList<>();

        String encode = passwordEncoder.encode("1234");

        list.add(new UserData(0, "raghu", "raghu@mail.com", encode, Set.of(Role.ADMIN), "", RateLimit.PREMIUM));
        list.add(new UserData(0, "lokesh", "lokesh@mail.com", encode, Set.of(Role.USER), "", RateLimit.NORMAL));
        list.add(new UserData(0, "muna", "muna@mail.com", encode, Set.of(Role.GUEST), "", RateLimit.FREE));

        if (userRepo.findById(1).isEmpty()) {
            userRepo.saveAll(list);
        } else {
            logger.info("Data Already Present in DB..");
        }

        if (userRepo.findAll().isEmpty()) logger.warn("Insertion Failed..!");

        logger.info("Start-up Method Execution Successfully Done..!");

        MDC.remove("userId");
    }
}
