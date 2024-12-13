package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.entities.Role;
import org.example.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Optional<Role> getUserRole(){
        return roleRepository.findByName("ROLE_USER");
    }

    public Optional<Role> getAdminRole(){
        return roleRepository.findByName("ROLE_ADMIN");
    }

    public void saveNewRole(String name){
        Role newRole = new Role();
        newRole.setName(name);
        roleRepository.save(newRole);
    }
}
