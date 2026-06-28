package com.aisdlc.auth;

import com.aisdlc.auth.dto.AuthResponse;
import com.aisdlc.auth.dto.LoginRequest;
import com.aisdlc.auth.dto.RegisterRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository  userRepo;
    private final PasswordEncoder encoder;
    private final JwtService      jwtService;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder, JwtService jwtService) {
        this.userRepo   = userRepo;
        this.encoder    = encoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username already taken: " + req.username());
        }
        if (userRepo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already registered: " + req.email());
        }
        UserEntity user = new UserEntity(
                req.username(),
                req.email(),
                encoder.encode(req.password()),
                "USER"
        );
        userRepo.save(user);
        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }

    public AuthResponse login(LoginRequest req) {
        UserEntity user = userRepo.findByUsername(req.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}
