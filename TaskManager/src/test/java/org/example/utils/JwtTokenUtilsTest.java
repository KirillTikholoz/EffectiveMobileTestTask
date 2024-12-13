package org.example.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JwtTokenUtils.class})
@TestPropertySource(properties = "jwt.secret-access-key=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
public class JwtTokenUtilsTest {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Value("${jwt.secret-access-key}")
    private String secretKey;
    String validToken;
    @BeforeEach
    public void setUp(){
        validToken =
                Jwts
                        .builder()
                        .subject("user@mail.ru")
                        .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .claim("roles", List.of("ROLE_ADMIN"))
                        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                        .compact();
    }
    @Test
    public void successfulGetEmail(){
        Assertions.assertEquals("user@mail.ru", jwtTokenUtils.getEmail(validToken));
    }

    @Test
    public void successfulGetRoles(){
        Assertions.assertEquals(List.of("ROLE_ADMIN"), jwtTokenUtils.getRoles(validToken));
    }

    @Test
    public void tokenThrowSignatureException(){
        String notValidKey = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
        String notValidToken =
                Jwts
                        .builder()
                        .subject("user@mail.ru")
                        .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .claim("roles", List.of("ROLE_ADMIN"))
                        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(notValidKey)))
                        .compact();

        Assertions.assertThrows(SignatureException.class, () ->
                jwtTokenUtils.getEmail(notValidToken));
    }

    @Test
    public void tokenThrowExpiredJwtException(){
        String validKey = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String notValidToken =
                Jwts
                        .builder()
                        .subject("user@mail.ru")
                        .expiration(new Date(System.currentTimeMillis() - 100))
                        .claim("roles", List.of("ROLE_ADMIN"))
                        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(validKey)))
                        .compact();

        Assertions.assertThrows(ExpiredJwtException.class, () ->
                jwtTokenUtils.getEmail(notValidToken));
    }
}
