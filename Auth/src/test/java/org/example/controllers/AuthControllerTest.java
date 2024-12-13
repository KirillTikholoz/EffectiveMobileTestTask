package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.example.configs.PasswordEncoderConfig;
import org.example.configs.SecurityConfig;
import org.example.dtos.JwtRequest;
import org.example.dtos.JwtResponse;
import org.example.services.AuthService;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {
        AuthController.class,
        GlobalExceptionHandler.class,
        PasswordEncoderConfig.class,
        SecurityConfig.class
})
public class AuthControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private AuthService authService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void successfulCreateJwtTokens() throws Exception {
        // given
        JwtRequest jwtRequest = new JwtRequest("test@mail.ru", "password");
        String jwtRequestJson = objectMapper.writeValueAsString(jwtRequest);
        String token = "access_token";
        ResponseEntity<JwtResponse> response = new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
        UserDetails userDetails = new User(
                jwtRequest.getEmail(),
                jwtRequest.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // when
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword())
        )).thenReturn(authentication);
        when(authService.createJwtTokens(jwtRequest.getEmail())).thenReturn(response);

        // then
        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void createJwtTokensWithBadCredentialsException() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("test@mail.ru", "password");
        String jwtRequestJson = objectMapper.writeValueAsString(jwtRequest);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtRequestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message")
                        .value("Incorrect email or password"));
    }

    @Test
    public void createJwtTokensWithEmptyPassword() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("test@mail.ru", "");
        String jwtRequestJson = objectMapper.writeValueAsString(jwtRequest);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Incorrect data was transmitted"));
    }

    @Test
    public void createJwtTokensWithIncorrectEmail() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("test", "password");
        String jwtRequestJson = objectMapper.writeValueAsString(jwtRequest);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Incorrect data was transmitted"));
    }

    @Test
    public void successfulRefreshJwtTokens() throws Exception {
        String refreshToken = "refresh_token";
        String accessToken = "access_token";
        JwtResponse jwtResponse = new JwtResponse(accessToken);

        when(authService.refreshJwtTokens(refreshToken)).thenReturn(ResponseEntity.ok(jwtResponse));

        mockMvc.perform(post("/refresh")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(accessToken));
    }

    @Test
    public void refreshJwtTokensWithoutRefreshToken() throws Exception {
        mockMvc.perform(post("/refresh"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void refreshJwtTokensWithIncorrectRefreshToken() throws Exception {
        String refreshToken = "refresh_token";

        when(authService.refreshJwtTokens(refreshToken)).thenThrow(new JwtException("Invalid jwt token"));

        mockMvc.perform(post("/refresh")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message")
                        .value("Invalid jwt token"));;
    }
}
