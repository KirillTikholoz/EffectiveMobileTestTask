package org.example.initializers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.services.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleInitializer implements CommandLineRunner {
    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        if (roleService.getUserRole().isEmpty()){
            roleService.saveNewRole("ROLE_USER");
        }

        if (roleService.getAdminRole().isEmpty()){
            roleService.saveNewRole("ROLE_ADMIN");
        }

        log.info("Standard roles have been created");
    }
}
