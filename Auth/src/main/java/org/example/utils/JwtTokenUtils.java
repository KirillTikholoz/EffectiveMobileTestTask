package org.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret-access-key}")
    private String secretAccessKey;
    @Value("${jwt.secret-refresh-key}")
    private String secretRefreshKey;
    @Value("${jwt.access-token-lifetime}")
    private Long jwtAccessTokenLifetime;
    @Value("${jwt.refresh-token-lifetime}")
    private Long jwtRefreshTokenLifetime;

    public String generateAccessToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        List<String> RoleList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", RoleList);

        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtAccessTokenLifetime))
                .signWith(getAccessSignKey())
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails){
        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtRefreshTokenLifetime))
                .signWith(getRefreshSignKey())
                .compact();
    }

    private Claims getAllClaimsFromToken(String token, SecretKey key){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Claims getAllClaimsFromAccessToken(String token){
        return getAllClaimsFromToken(token, getAccessSignKey());
    }

    private Claims getAllClaimsFromRefreshToken(String token){
        return getAllClaimsFromToken(token, getRefreshSignKey());
    }

    private SecretKey getAccessSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretAccessKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getRefreshSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretRefreshKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getEmailFromRefreshToken(String token){
        return getAllClaimsFromRefreshToken(token).getSubject();
    }
}
