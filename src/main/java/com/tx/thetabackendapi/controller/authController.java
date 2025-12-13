package com.tx.thetabackendapi.controller;

import com.tx.thetabackendapi.dto.authResponse;
import com.tx.thetabackendapi.dto.loginRequest;
import com.tx.thetabackendapi.dto.registerRequest;
import com.tx.thetabackendapi.model.user;
import com.tx.thetabackendapi.repository.userRepository;
import com.tx.thetabackendapi.security.jwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/auth")
public class authController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final jwtUtil jwtUtil;
    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public authController(AuthenticationManager authenticationManager,UserDetailsService userDetailsService,jwtUtil jwtUtil,userRepository userRepository,PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/login")
    public ResponseEntity<authResponse> login(@Valid @RequestBody loginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
            user appUser = userRepository.findByUsername(request.username()).orElseThrow(() -> new BadCredentialsException("User not found"));
            String token = jwtUtil.buildToken(userDetails, appUser.getUserId(), appUser.getRole());
            return ResponseEntity.ok(authResponse.success(token,appUser.getUsername(),appUser.getUserId(),appUser.getRole()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse.error("Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<authResponse> register(@Valid @RequestBody registerRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(authResponse.error("Username already exists"));
        }
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(authResponse.error("Email already exists"));
        }

        user newUser = new user();
        newUser.setUsername(request.username());
        newUser.setEmail(request.email());
        newUser.setPasswordHash(passwordEncoder.encode(request.password()));
        newUser.setRole("base");
        newUser.setDateCreated(LocalDateTime.now());

        user savedUser = userRepository.save(newUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String token = jwtUtil.buildToken(userDetails, savedUser.getUserId(), savedUser.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse.registered(token,savedUser.getUsername(),savedUser.getUserId(),savedUser.getRole()));
    }
    @GetMapping("/validate")
    public ResponseEntity<Boolean> sessionValid(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }
    
}