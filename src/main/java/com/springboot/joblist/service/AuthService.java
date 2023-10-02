package com.springboot.joblist.service;


import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.joblist.dto.request.LoginUserRequest;
import com.springboot.joblist.dto.request.RegisterUserRequest;
import com.springboot.joblist.dto.response.TokenResponse;
import com.springboot.joblist.dto.response.UserResponse;
import com.springboot.joblist.entity.Role;
import com.springboot.joblist.entity.User;
import com.springboot.joblist.entity.UserInfoDetails;
import com.springboot.joblist.entity.utils.EnumRole;
import com.springboot.joblist.exception.RequestIsNotValidException;
import com.springboot.joblist.exception.UserNotFoundException;
import com.springboot.joblist.repository.RoleRepository;
import com.springboot.joblist.repository.UserRepository;
import com.springboot.joblist.security.jwt.JwtUtils;
import com.springboot.joblist.service.utils.UserMapper;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final ValidationService validationService;


    @Transactional
    public UserResponse register(RegisterUserRequest registerUserRequest) {
        validationService.validate(registerUserRequest);
        if(userRepository.existsByEmail(registerUserRequest.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }
        registerUserRequest.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        Set<Role> roles = this.getRoles();
        User user = UserMapper.toUser(registerUserRequest, roles);
        userRepository.save(user);
        return UserMapper.fromUser(user);
    }

    @Transactional
    public TokenResponse authenticate(LoginUserRequest loginUserRequest) {
        validationService.validate(loginUserRequest);
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginUserRequest.getEmail(),
                loginUserRequest.getPassword()
            )
        );
        User user = userRepository.findByEmail(loginUserRequest.getEmail())
            .orElseThrow();
        UserDetails userDetails = new UserInfoDetails(user);
        var jwtToken = jwtUtils.generateToken(userDetails);
        var refreshToken = jwtUtils.generateRefreshToken(userDetails);
        return TokenResponse.builder()
            .token(jwtToken)
            .expiresIn(30*60*1000)
            .refreshToken(refreshToken)
            .build();
    }

    public TokenResponse refreshToken(HttpServletRequest request) throws IOException, UserNotFoundException, RequestIsNotValidException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new RequestIsNotValidException("Authorization Header Type should be bearer");
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtUtils.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("User with email: "+userEmail+" is not found."));
            UserDetails userDetails = new UserInfoDetails(user);
            if (jwtUtils.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtUtils.generateToken(userDetails);
                TokenResponse authResponse = TokenResponse.builder()
                        .token(accessToken)
                        .expiresIn(30*60*100)
                        .refreshToken(refreshToken)
                        .build();
                return authResponse;
            }
        }
        return null;
    }

    private Set<Role> getRoles(){
        Set<Role> roles = new HashSet<>();
        if(!roleRepository.existsByName(EnumRole.ADMIN)){
            Role adminRole = new Role(EnumRole.ADMIN);
            roleRepository.save(adminRole);
        }

        Role adminRole = roleRepository.findByName(EnumRole.ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(adminRole);
        return roles;
    }
}

