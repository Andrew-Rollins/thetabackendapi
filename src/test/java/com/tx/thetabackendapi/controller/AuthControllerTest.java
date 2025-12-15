package com.tx.thetabackendapi.controller;
//Run this by running gradle clean
import com.tx.thetabackendapi.dto.authResponse;
import com.tx.thetabackendapi.dto.loginRequest;
import com.tx.thetabackendapi.dto.registerRequest;
import com.tx.thetabackendapi.model.user;
import com.tx.thetabackendapi.repository.userRepository;
import com.tx.thetabackendapi.security.jwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    //what we are doing here is emulating the database server to make the tests easier and quicker
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private jwtUtil jwtUtil;
    @Mock
    private userRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private authController authController;
    //test objects
    private user userProxy;
    private UserDetails userDetailsProxy;
    private Authentication authenticationProxy;
    //runs test for every auth situation the app might find itself in
    @BeforeEach
    void init() {
        //creates a basic user before the tests 
        userProxy = new user();
        userProxy.setUserId(0);
        userProxy.setUsername("test");
        userProxy.setEmail("test@test.com");
        userProxy.setPasswordHash("password");
        userProxy.setRole("base");
        userDetailsProxy = User.builder().username("test").password("password").authorities(new ArrayList<>()).build();
        authenticationProxy = new UsernamePasswordAuthenticationToken(userDetailsProxy, "password", new ArrayList<>());
    }

    @Test
    void loginCorrect() {
        loginRequest request = new loginRequest("test@test.com", "password");
        //mockito strucure for tests
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(userProxy));
        when(authenticationManager.authenticate(any())).thenReturn(authenticationProxy);
        when(userDetailsService.loadUserByUsername("test")).thenReturn(userDetailsProxy);
        when(jwtUtil.buildToken(any(), eq(0), eq("base"))).thenReturn("faketoken");
        ResponseEntity<authResponse> response = authController.login(request);
        //check if logged in
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userRepository).findByEmail("test@test.com");
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void loginNoUser() {
        loginRequest request = new loginRequest("fakeuser@test.com", "password");
        when(userRepository.findByEmail("fakeuser@test.com")).thenReturn(Optional.empty());
        ResponseEntity<authResponse> response = authController.login(request);
        //check if logged in
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userRepository).findByEmail("fakeuser@test.com");
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void registerCorrect() {
        registerRequest request = new registerRequest("newuser", "newuser@test.com", "password");
        user registerUser = new user();
        registerUser.setUserId(1);
        registerUser.setUsername("newuser");
        registerUser.setEmail("newuser@test.com");
        registerUser.setPasswordHash("encoded");
        registerUser.setRole("base");
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(userRepository.save(any(user.class))).thenReturn(registerUser);
        when(userDetailsService.loadUserByUsername("newuser")).thenReturn(userDetailsProxy);
        when(jwtUtil.buildToken(any(), eq(1), eq("base"))).thenReturn("faketoken");
        ResponseEntity<authResponse> response = authController.register(request);
        //check if registered
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userRepository).save(any(user.class));
        verify(passwordEncoder).encode("password");
    }

    @Test
    void registerSameUsername() {
        registerRequest request = new registerRequest("test", "newuser@test.com", "password");
        when(userRepository.existsByUsername("test")).thenReturn(true);
        ResponseEntity<authResponse> response = authController.register(request);
        //check if register fails on dupe username
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(userRepository).existsByUsername("test");
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerSameEmail() {
        registerRequest request = new registerRequest("newuser", "test@test.com", "password");
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);
        ResponseEntity<authResponse> response = authController.register(request);
        //check if register fails on dupe email
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(userRepository).existsByEmail("test@test.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void sessionValid() {
        ResponseEntity<Boolean> response = authController.sessionValid(userDetailsProxy);
        //check if session valid
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void sessionInvalid() {
        ResponseEntity<Boolean> response = authController.sessionValid(null);
        //check if session invalid
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody());
    }
}