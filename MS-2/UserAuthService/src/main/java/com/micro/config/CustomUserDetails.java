package com.micro.config;

import com.micro.entity.Role;
import com.micro.entity.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private UserData userData;

    public CustomUserDetails(UserData userCredential) {
        this.userData = userCredential;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        HashSet<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();

        Set<Role> roles = userData.getRoles();

        for(Role r : roles){
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(r.toString()));
        }

//        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(userData.getRole().toString()));

        return simpleGrantedAuthorities;
    }

    @Override
    public String getPassword() {
        return userData.getPassword();
    }

    @Override
    public String getUsername() {
        return userData.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}