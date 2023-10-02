package com.springboot.joblist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.joblist.dto.request.LoginUserRequest;
import com.springboot.joblist.dto.request.RegisterUserRequest;
import com.springboot.joblist.dto.response.TokenResponse;
import com.springboot.joblist.dto.response.UserResponse;
import com.springboot.joblist.exception.RequestIsNotValidException;
import com.springboot.joblist.exception.UserNotFoundException;
import com.springboot.joblist.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponse> register(@RequestBody RegisterUserRequest registerUserRequest){
        UserResponse userResponse = authService.register(registerUserRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TokenResponse> login(@RequestBody LoginUserRequest loginUserRequest, HttpServletResponse response){
        TokenResponse tokenResponse = authService.authenticate(loginUserRequest);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) throws io.jsonwebtoken.io.IOException, UserNotFoundException, RequestIsNotValidException {
        TokenResponse tokenResponse = authService.refreshToken(request);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }
}

