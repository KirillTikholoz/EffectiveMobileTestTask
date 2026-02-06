package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.descriptions.AuthApi;
import org.example.dtos.JwtRequest;
import org.example.dtos.JwtResponse;
import org.example.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthApi {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> createJwtTokens(@Valid @RequestBody JwtRequest jwtRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword())
        );
        return authService.createJwtTokens(jwtRequest.getEmail());
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshJwtTokens(
            @CookieValue(value = "refreshToken") String refreshTokenArg
    ){
        return authService.refreshJwtTokens(refreshTokenArg);
    }
}
