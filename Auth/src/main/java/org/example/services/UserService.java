package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dtos.RegistrationUserDto;
import org.example.entities.Role;
import org.example.entities.User;
import org.example.exceptions.PasswordMismatchException;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.mappers.impl.UserMapper;
import org.example.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleService roleService;

    private void createNewUser(RegistrationUserDto registrationUserDto, Role role){
        if (userRepository.findByEmail(registrationUserDto.getEmail()).isPresent()){
            throw new UserAlreadyExistsException(
                    String.format("Пользователь с email: %s уже зарегистрирован", registrationUserDto.getEmail())
            );
        }

        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())){
            throw new PasswordMismatchException("Пароли не совпадают");
        }

        User newUser = userMapper.toEntity(registrationUserDto);
        newUser.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        newUser.setRoles(List.of(role));
        userRepository.save(newUser);
    }

    @Transactional
    public void createUser(RegistrationUserDto registrationUserDto){
        createNewUser(registrationUserDto, roleService.getUserRole().orElseThrow(() ->
                new NoSuchElementException("Данной роли не существует")));
    }

    @Transactional
    public void createAdmin(RegistrationUserDto userDto){
        createNewUser(userDto, roleService.getAdminRole().orElseThrow(() ->
                new NoSuchElementException("Данной роли не существует")));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь с таким email: '%s' не найден", email)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
    }
}
