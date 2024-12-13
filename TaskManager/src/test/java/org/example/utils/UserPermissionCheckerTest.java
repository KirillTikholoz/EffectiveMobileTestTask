package org.example.utils;

import org.example.entitis.Task;
import org.example.enums.Priority;
import org.example.enums.Status;
import org.example.exception.UserNotEnoughAuthorities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPermissionCheckerTest {
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private UserPermissionChecker userPermissionChecker;
    @Test
    public void checkUserPermissionsWithUserNotEnoughAuthorities(){
        Task task = new Task(
                1L,
                "title",
                "description",
                Status.COMPLETED,
                Priority.MEDIUM,
                "user1@mail.ru",
                "Kirill@mail.ru",
                new LinkedList<>()
        );
        UserDetails userDetails = new User(
                "another_user",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        Assertions.assertThrows(UserNotEnoughAuthorities.class, () ->
                userPermissionChecker.checkUserPermissions(task));
    }
}
