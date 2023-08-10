package com.micro.util;

import com.micro.entity.Role;
import com.micro.entity.UserData;
import com.micro.repo.UserRepo;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyStartupTack implements ApplicationRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<UserData> list = new ArrayList<>();

        String encode = passwordEncoder.encode("1234");

        list.add(new UserData(0,"raghu","raghu@mail.com",encode,Role.ROLE_ADMIN));
        list.add(new UserData(0,"lokesh","lokesh@mail.com",encode,Role.ROLE_USER));
        list.add(new UserData(0,"muna","muna@mail.com",encode,Role.ROLE_GUEST));

        userRepo.saveAll(list);

        System.out.println(userRepo.findAll());
    }
}
