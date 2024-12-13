package org.example.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JwtTokenUtils.class})
@TestPropertySource(properties = {
        "jwt.secret-access-key=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        "jwt.secret-refresh-key=bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
        "jwt.access-token-lifetime=600000",
        "jwt.refresh-token-lifetime=600000"
})
public class JwtTokenUtilsTest {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Test
    public void successfulGetEmailFromRefreshToken(){
        UserDetails userDetails = new User(
                "user@mail.ru",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        String refreshToken = jwtTokenUtils.generateRefreshToken(userDetails);

        Assertions.assertEquals("user@mail.ru", jwtTokenUtils.getEmailFromRefreshToken(refreshToken));
    }

    @Test
    public void getEmailFromRefreshTokenWithIncorrectSignature(){
        String notValidSecretRefreshKey = "cccccccccccccccccccccccccccccccccccccccccccc";
        SecretKey signature = Keys.hmacShaKeyFor(Decoders.BASE64.decode(notValidSecretRefreshKey));

        String notValidToken =
                Jwts
                        .builder()
                        .subject("user@mail.ru")
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + 600000))
                        .signWith(signature)
                        .compact();

        Assertions.assertThrows(JwtException.class, () ->
                jwtTokenUtils.getEmailFromRefreshToken(notValidToken));
    }

    @Test
    public void getEmailFromRefreshWithExpiredLifetime(){
        String validSecretRefreshKey = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
        SecretKey signature = Keys.hmacShaKeyFor(Decoders.BASE64.decode(validSecretRefreshKey));

        String notValidToken =
                Jwts
                        .builder()
                        .subject("user@mail.ru")
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() - 600000))
                        .signWith(signature)
                        .compact();

        Assertions.assertThrows(JwtException.class, () ->
                jwtTokenUtils.getEmailFromRefreshToken(notValidToken));
    }
}
