package com.micro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.w3c.dom.html.HTMLTableRowElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;

    @Column(unique = true, name = "username")
    private String name;

    @Column(name = "mail")
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Column(name = "api_key")
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = "rate_limit")
    private RateLimit limit;
}
