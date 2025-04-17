package com.springboot.blog.controller;

import com.springboot.blog.dto.JWTAuthResponse;
import com.springboot.blog.dto.LoginDto;
import com.springboot.blog.dto.RegisterDto;
import com.springboot.blog.service.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/login","/signin"})
    public ResponseEntity<JWTAuthResponse> logIn(@RequestBody LoginDto loginDto){

        String token=authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse=new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);

    }

    @PostMapping(value = {"/signup","/register"})
    public ResponseEntity<String> signUp(@RequestBody RegisterDto registerDto){

        String response=authService.register(registerDto);
        return ResponseEntity.ok(response);

    }



}
