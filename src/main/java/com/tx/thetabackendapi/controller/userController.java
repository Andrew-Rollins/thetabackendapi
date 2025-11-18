package com.tx.thetabackendapi.controller;

import com.tx.thetabackendapi.model.user;
import com.tx.thetabackendapi.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class userController {
    
    @Autowired
    private userRepository userRepository;
    
    @GetMapping
    public List<user> getAllUsers() {
        return userRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public user getUser(@PathVariable Integer id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @PostMapping
    public user createUser(@RequestBody user user) {
        return userRepository.save(user);
    }
    
    @PutMapping("/{id}")
    public user updateUser(@PathVariable Integer id, @RequestBody user user) {
        user.setUserId(id);
        return userRepository.save(user);
    }
    
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }
}