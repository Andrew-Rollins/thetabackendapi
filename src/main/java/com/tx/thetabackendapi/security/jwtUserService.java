package com.tx.thetabackendapi.security;

import com.tx.thetabackendapi.model.user;
import com.tx.thetabackendapi.repository.userRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class jwtUserService implements UserDetailsService {
    //connecting user models
    private final userRepository userRepository;
    public jwtUserService(userRepository userRepository) {
        this.userRepository = userRepository;
    }
    //glue database entry into user model 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("NO USER: " + username));
        return new User(user.getUsername(), user.getPasswordHash(), List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())));
    }
}