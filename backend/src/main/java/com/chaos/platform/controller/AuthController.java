package com.chaos.platform.controller;

import com.chaos.platform.model.dto.LoginRequest;
import com.chaos.platform.model.dto.LoginResponse;
import com.chaos.platform.security.JwtTokenProvider;
import com.chaos.platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        String token = tokenProvider.generateToken(request.getUsername());
        return ResponseEntity.ok(new LoginResponse(token));
    }
} 