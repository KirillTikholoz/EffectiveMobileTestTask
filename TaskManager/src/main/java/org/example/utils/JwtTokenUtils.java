package org.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret-access-key}")
    private String secretAccessKey;

    private Claims getAllClaimsFromAccessToken(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretAccessKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getEmail(String token){
        return getAllClaimsFromAccessToken(token).getSubject();
    }

    public List<String> getRoles(String token){
        return getAllClaimsFromAccessToken(token).get("roles", List.class);
    }
}
