package com.jwt.auth.service.service.impl;

import com.jwt.auth.service.model.UserAuth;
import com.jwt.auth.service.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserAuthRepository userAuthRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuth user = userAuthRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<String> roles = user.getRoles().stream()
                .map(name -> name.getName().toUpperCase())
                .toList();
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(roles.toArray(String[]::new))
                .build();
    }
}
