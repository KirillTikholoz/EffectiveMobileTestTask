package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.configs.PasswordEncoderConfig;
import org.example.configs.SecurityConfig;
import org.example.dtos.RegistrationUserDto;
import org.example.exceptions.PasswordMismatchException;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
@ContextConfiguration(classes = {
        RegistrationController.class,
        GlobalExceptionHandler.class,
        PasswordEncoderConfig.class,
        SecurityConfig.class
})
public class RegistrationControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void successfulRegistrationUser() throws Exception {
        RegistrationUserDto registrationUserDto =
                new RegistrationUserDto("test@mail.ru", "password", "password");
        String registrationUserDtoJson = objectMapper.writeValueAsString(registrationUserDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationUserDtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("The user has been successfully registered"));
    }

    @Test
    public void successfulRegistrationAdmin() throws Exception {
        RegistrationUserDto registrationUserDto =
                new RegistrationUserDto("test@mail.ru", "password", "password");
        String registrationUserDtoJson = objectMapper.writeValueAsString(registrationUserDto);

        mockMvc.perform(post("/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationUserDtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("The administrator has been successfully registered"));
    }

    @Test
    public void registrationUserWithPasswordMismatchException() throws Exception {
        RegistrationUserDto registrationUserDto =
                new RegistrationUserDto("test@mail.ru", "password", "incorrect_password");
        String registrationUserDtoJson = objectMapper.writeValueAsString(registrationUserDto);

        doThrow(new PasswordMismatchException())
                .when(userService).createUser(registrationUserDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationUserDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Password and confirmation password do not match"));
    }

    @Test
    public void registrationUserWithUserAlreadyExistsException() throws Exception {
        RegistrationUserDto registrationUserDto =
                new RegistrationUserDto("test@mail.ru", "password", "password");
        String registrationUserDtoJson = objectMapper.writeValueAsString(registrationUserDto);

        doThrow(new UserAlreadyExistsException())
                .when(userService).createUser(registrationUserDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationUserDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Such a user already exists"));
    }

    @Test
    public void registrationUserWithEmptyPassword() throws Exception {
        RegistrationUserDto registrationUserDto =
                new RegistrationUserDto("test@mail.ru", "", "");
        String registrationUserDtoJson = objectMapper.writeValueAsString(registrationUserDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationUserDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Incorrect data was transmitted"));
    }

    @Test
    public void registrationUserWithIncorrectEmail() throws Exception {
        RegistrationUserDto registrationUserDto =
                new RegistrationUserDto("test", "password", "password");
        String registrationUserDtoJson = objectMapper.writeValueAsString(registrationUserDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationUserDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Incorrect data was transmitted"));
    }
}
