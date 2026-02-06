package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.descriptions.RegistrationApi;
import org.example.dtos.RegistrationUserDto;
import org.example.dtos.SuccessfulUserRegistrationDto;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RegistrationController implements RegistrationApi {
    private final UserService userService;

    @PostMapping("user/register")
    public ResponseEntity<SuccessfulUserRegistrationDto> registrationUser(
            @Valid @RequestBody RegistrationUserDto registrationUserDto
    ){
        userService.createUser(registrationUserDto);

        return new ResponseEntity<>(
                new SuccessfulUserRegistrationDto("The user has been successfully registered"),
                HttpStatus.CREATED
        );
    }

    @PostMapping("admin/register")
    public ResponseEntity<SuccessfulUserRegistrationDto> registrationAdmin(
            @Valid @RequestBody RegistrationUserDto registrationUserDto
    ){
        userService.createAdmin(registrationUserDto);

        return new ResponseEntity<>(
                new SuccessfulUserRegistrationDto("The administrator has been successfully registered"),
                HttpStatus.CREATED
        );
    }
}
