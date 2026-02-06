package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.entities.Role;
import org.example.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Optional<Role> getUserRole(){
        return roleRepository.findByName("ROLE_USER");
    }

    @Transactional(readOnly = true)
    public Optional<Role> getAdminRole(){
        return roleRepository.findByName("ROLE_ADMIN");
    }

    @Transactional
    public void saveNewRole(String name){
        Role newRole = new Role();
        newRole.setName(name);
        roleRepository.save(newRole);
    }
}
