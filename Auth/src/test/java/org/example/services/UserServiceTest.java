package org.example.services;

import org.example.dtos.RegistrationUserDto;
import org.example.entities.Role;
import org.example.entities.User;
import org.example.exceptions.PasswordMismatchException;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private UserService userService;

    @Test
    public void successfulCreateUser(){
        RegistrationUserDto registrationUserDto =
                new RegistrationUserDto("test@mail.ru", "password", "password");
        Role role = new Role(null, "User");
        User user = new User(null, "test@mail.ru", "encodePassword", List.of(role));

        when(roleService.getUserRole()).thenReturn(Optional.of(role));
        when(userRepository.findByEmail(registrationUserDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodePassword");

        userService.createUser(registrationUserDto);
        verify(userRepository).save(user);
    }

    @Test
    public void createUserWithUserAlreadyExistsException(){
        RegistrationUserDto registrationUserDto =
                new RegistrationUserDto("test@mail.ru", "password", "password");
        Role role = new Role(null, "User");
        User user = new User(null, "test@mail.ru", "encodePassword", List.of(role));

        when(roleService.getUserRole()).thenReturn(Optional.of(role));
        when(userRepository.findByEmail(registrationUserDto.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                userService.createUser(registrationUserDto));
    }

    @Test
    public void createUserWithPasswordMismatchException(){
        RegistrationUserDto registrationUserDto =
                new RegistrationUserDto("test@mail.ru", "password", "incorrectPassword");
        Role role = new Role(null, "User");

        when(roleService.getUserRole()).thenReturn(Optional.of(role));
        when(userRepository.findByEmail(registrationUserDto.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(PasswordMismatchException.class, () ->
                userService.createUser(registrationUserDto));
    }

    @Test
    public void successfulCreateAdmin(){
        RegistrationUserDto registrationUserDto =
                new RegistrationUserDto("test@mail.ru", "password", "password");
        Role role = new Role(null, "Admin");
        User user = new User(null, "test@mail.ru", "encodePassword", List.of(role));

        when(roleService.getAdminRole()).thenReturn(Optional.of(role));
        when(userRepository.findByEmail(registrationUserDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodePassword");

        userService.createAdmin(registrationUserDto);
        verify(userRepository).save(user);
    }

    @Test
    void successfulLoadUserByUsername() {
        String email = "test@mail.ru";
        String password = "encodedPassword";
        Role role = new Role(null, "ROLE_USER");
        User user = new User(null, email, password, List.of(role));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(email);
        Assertions.assertNotNull(userDetails);
    }

    @Test
    void loadUserByUsernameWithUsernameNotFoundException() {
        String email = "test@mail.ru";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername(email));
    }
}
