package org.example.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.dtos.JwtResponse;
import org.example.utils.JwtTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    private void setRefreshTokenCookie(String refreshToken){

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setMaxAge(604800);
        refreshTokenCookie.setHttpOnly(true);
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        response.addCookie(refreshTokenCookie);
    }

    public ResponseEntity<JwtResponse> createJwtTokens(String email){
        UserDetails userDetails = userService.loadUserByUsername(email);

        String accessToken = jwtTokenUtils.generateAccessToken(userDetails);
        String refreshToken = jwtTokenUtils.generateRefreshToken(userDetails);
        setRefreshTokenCookie(refreshToken);

        return new ResponseEntity<>(new JwtResponse(accessToken), HttpStatus.OK);
    }

    public ResponseEntity<JwtResponse> refreshJwtTokens(String refreshToken){
        String email = jwtTokenUtils.getEmailFromRefreshToken(refreshToken);
        return createJwtTokens(email);
    }
}
