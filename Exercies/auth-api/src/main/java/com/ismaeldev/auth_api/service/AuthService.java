package com.ismaeldev.auth_api.service;

import com.ismaeldev.auth_api.dto.LoginRequest;
import com.ismaeldev.auth_api.dto.RegisterRequest;
import com.ismaeldev.auth_api.dto.TokenResponse;
import com.ismaeldev.auth_api.entity.UserAccount;
import com.ismaeldev.auth_api.repository.UserRepository;
import com.ismaeldev.auth_api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public TokenResponse register(RegisterRequest request){
        UserAccount user = new UserAccount();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassWord(passwordEncoder.encode(request.password()));

        repository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return new TokenResponse(jwtToken);
    }

    public TokenResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(),request.password())
        );
        UserAccount user = repository.findByEmail(request.email()).orElseThrow(
                () -> new IllegalArgumentException("Email introuvable")
        );
        String jwtToken = jwtService.generateToken(user);
        return new TokenResponse(jwtToken);
    }
}
