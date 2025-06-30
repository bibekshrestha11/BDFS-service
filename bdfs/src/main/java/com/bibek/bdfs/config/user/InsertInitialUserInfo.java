package com.bibek.bdfs.config.user;

import com.bibek.bdfs.user.entity.BloodGroup;
import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.user.repository.UserRepository;
import com.bibek.bdfs.user.role.entity.UserRole;
import com.bibek.bdfs.user.role.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(2)
public class InsertInitialUserInfo implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_PASSWORD = "Admin@123";
    private static final String USER_PASSWORD = "User@123";

    private static final String ADMIN_EMAIL = "admin@bdfs.com";
    private static final String USER_EMAIL = "user@bdfs.com";

    @Override
    public void run(String... args) {
        if (userRepository.findByEmailId(ADMIN_EMAIL).isEmpty()) {
            User admin = new User();
            admin.setFullName("Admin User");
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setRoles(List.of(
                    rolesRepository.findByName(UserRole.ADMIN.toString())
                            .orElseThrow(() -> new RuntimeException("ADMIN Role not found"))
            ));
            admin.setEmailId(ADMIN_EMAIL);
            admin.setPhoneNumber("01-4217666");
            admin.setLocation("Kathmandu, Nepal");
            admin.setBirthDate(LocalDate.parse("1990-01-01"));
            admin.setBloodGroup(BloodGroup.O_NEGATIVE);
            admin.setLatitude(27.7172);
            admin.setLongitude(85.324);
            admin.setVerified(true);
            userRepository.save(admin);
            log.info("Admin user inserted.");
        } else {
            log.info("Admin user already exists.");
        }

        if (userRepository.findByEmailId(USER_EMAIL).isEmpty()) {
            User user = new User();
            user.setFullName("Regular User");
            user.setPassword(passwordEncoder.encode(USER_PASSWORD));
            user.setRoles(List.of(
                    rolesRepository.findByName(UserRole.USER.toString())
                            .orElseThrow(() -> new RuntimeException("USER Role not found"))
            ));
            user.setEmailId(USER_EMAIL);
            user.setPhoneNumber("01-4217664");
            user.setLocation("Chabahil, Kathmandu");
            user.setBirthDate(LocalDate.parse("1990-01-01"));
            user.setBloodGroup(BloodGroup.O_NEGATIVE);
            user.setLatitude(27.7172);
            user.setLongitude(85.324);
            user.setVerified(true);
            userRepository.save(user);
            log.info("Regular user inserted.");
        } else {
            log.info("Regular user already exists.");
        }
    }
}
