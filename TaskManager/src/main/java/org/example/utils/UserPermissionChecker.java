package org.example.utils;

import org.example.entitis.Task;
import org.example.exception.UserNotEnoughAuthorities;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionChecker {
    public void checkUserPermissions(Task task) {
        SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .forEach(role -> {
                    if (role.equals("ROLE_USER")){
                        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                        if (!task.getExecutor().equals(authUserEmail)){
                            throw new UserNotEnoughAuthorities("The user does not have enough authorities");
                        }
                    }
                });
    }
}
