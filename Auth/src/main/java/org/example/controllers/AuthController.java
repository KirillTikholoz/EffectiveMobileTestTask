package org.example.controllers;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.descriptions.AuthApi;
import org.example.dtos.ErrorResponseDto;
import org.example.dtos.JwtRequest;
import org.example.dtos.JwtResponse;
import org.example.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto("Bad Request", "Required cookie 'refreshToken' is not present"),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> badCredentialsExceptionHandler(BadCredentialsException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto("Unauthorized", "Incorrect email or password"),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDto> jwtExceptionHandler(JwtException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto("Unauthorized", "Invalid jwt token"),
                HttpStatus.UNAUTHORIZED
        );
    }
}
