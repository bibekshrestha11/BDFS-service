package com.bibek.bdfs.user.role.config;

import com.bibek.bdfs.user.role.entity.Roles;
import com.bibek.bdfs.user.role.entity.UserRole;
import com.bibek.bdfs.user.role.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class InitialRolesConfig implements CommandLineRunner {

    private final RolesRepository rolesRepository;

    @Override
    public void run(String... args) throws Exception {
        if (rolesRepository.count() < 2) {
            log.info("Roles creation request received");
            createRoleIfNotExists(UserRole.ADMIN.name(), "The admin of the application ");
            createRoleIfNotExists(UserRole.USER.name(), "The user of the application ");
            log.info("Roles created");
        } else {
            log.info("Roles already exist");
        }
    }

    private void createRoleIfNotExists(String name, String description) {
        if (rolesRepository.findByName(name).isEmpty()) {
            Roles role = Roles.builder()
                    .name(name)
                    .description(description)
                    .build();
            rolesRepository.save(role);
            log.info("Role {} inserted", name);
        }
    }
}