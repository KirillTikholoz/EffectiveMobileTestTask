package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.configs.SecurityConfig;
import org.example.dtos.CommentDto;
import org.example.filters.JwtRequestFilter;
import org.example.services.CommentService;
import org.example.utils.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@ContextConfiguration(classes = {
        CommentController.class,
        SecurityConfig.class,
        JwtRequestFilter.class,
        JwtTokenUtils.class,
        GlobalExceptionHandler.class
})
@ActiveProfiles("test")
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CommentService commentService ;
    @Value("${jwt.secret-access-key}")
    private String secretKey;
    String validAdminToken;
    @BeforeEach
    public void setUp(){
        validAdminToken =
                Jwts
                        .builder()
                        .subject("user@mail.ru")
                        .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .claim("roles", List.of("ROLE_ADMIN"))
                        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                        .compact();
    }
    @Test
    public void successfulCreateTaskHandler() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "text");
        String commentDtoJson = objectMapper.writeValueAsString(commentDto);

        mockMvc.perform(post("/comments")
                        .header("Authorization", "Bearer " + validAdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentDtoJson))
                .andExpect(status().isCreated());
    }
}
