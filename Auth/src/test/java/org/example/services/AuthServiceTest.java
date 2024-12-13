package org.example.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dtos.JwtResponse;
import org.example.utils.JwtTokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtTokenUtils jwtTokenUtils;
    @InjectMocks
    private AuthService authService;
    @BeforeEach
    public void setUp(){
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(mock(HttpServletRequest.class), mockResponse);
        RequestContextHolder.setRequestAttributes(requestAttributes);
    }

    @Test
    public void successfulCreateJwtTokens(){
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String email = "user@mail.ru";
        UserDetails userDetails = new User(
                email,
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        ResponseEntity<JwtResponse> response = new ResponseEntity<>(new JwtResponse(accessToken), HttpStatus.OK);

        when(userService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtTokenUtils.generateAccessToken(userDetails)).thenReturn(accessToken);
        when(jwtTokenUtils.generateRefreshToken(userDetails)).thenReturn(refreshToken);

        Assertions.assertEquals(response, authService.createJwtTokens(email));
    }

    @Test
    public void successfulRefreshJwtTokens(){
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String email = "user@mail.ru";
        UserDetails userDetails = new User(
                email,
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        ResponseEntity<JwtResponse> response = new ResponseEntity<>(new JwtResponse(accessToken), HttpStatus.OK);

        when(jwtTokenUtils.getEmailFromRefreshToken(refreshToken)).thenReturn(email);
        when(userService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtTokenUtils.generateAccessToken(userDetails)).thenReturn(accessToken);
        when(jwtTokenUtils.generateRefreshToken(userDetails)).thenReturn(refreshToken);

        Assertions.assertEquals(response, authService.refreshJwtTokens(refreshToken));
    }

}
