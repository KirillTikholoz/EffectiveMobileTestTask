package org.example.services;

import org.example.entities.Role;
import org.example.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleService roleService;

    @Test
    public void getUserRoleTest(){
        String roleName = "ROLE_USER";
        Role role = new Role(null, roleName);

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));

        Assertions.assertEquals(Optional.of(role), roleService.getUserRole());
    }

    @Test
    public void getAdminRoleTest(){
        String roleName = "ROLE_ADMIN";
        Role role = new Role(null, roleName);

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));

        Assertions.assertEquals(Optional.of(role), roleService.getAdminRole());
    }

    @Test
    public void successfulSaveNewRole(){
        String roleName = "ROLE_ADMIN";
        roleService.saveNewRole(roleName);
        verify(roleRepository).save(any(Role.class));
    }
}
