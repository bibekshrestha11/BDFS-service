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
    private static final String DEFAULT_PASSWORD = "Admin@123";

    @Override
    public void run(String... args) {
        // Existing admin and user insertion (unchanged) ...

        // 3 users in Chabahil
        insertUserIfNotExists("Chabahil User 1", "chabahil1@yopmail.com", "9800000011", BloodGroup.A_POSITIVE, 27.7110, 85.3290, "Chabahil, Kathmandu");
        insertUserIfNotExists("Chabahil User 2", "chabahil2@yopmail.com", "9800000012", BloodGroup.B_NEGATIVE, 27.7125, 85.3305, "Chabahil, Kathmandu");
        insertUserIfNotExists("Chabahil User 3", "chabahil3@yopmail.com", "9800000013", BloodGroup.O_POSITIVE, 27.7135, 85.3315, "Chabahil, Kathmandu");

        // 3 users in Boudha
        insertUserIfNotExists("Boudha User 1", "boudha1@yopmail.com", "9800000014", BloodGroup.AB_POSITIVE, 27.7210, 85.3440, "Boudha, Kathmandu");
        insertUserIfNotExists("Boudha User 2", "boudha2@yopmail.com", "9800000015", BloodGroup.O_NEGATIVE, 27.7225, 85.3455, "Boudha, Kathmandu");
        insertUserIfNotExists("Boudha User 3", "boudha3@yopmail.com", "9800000016", BloodGroup.B_POSITIVE, 27.7235, 85.3465, "Boudha, Kathmandu");

        // 3 users in Ratna Park
        insertUserIfNotExists("Ratna Park User 1", "ratnapark1@yopmail.com", "9800000017", BloodGroup.A_NEGATIVE, 27.7040, 85.3110, "Ratna Park, Kathmandu");
        insertUserIfNotExists("Ratna Park User 2", "ratnapark2@yopmail.com", "9800000018", BloodGroup.AB_NEGATIVE, 27.7055, 85.3125, "Ratna Park, Kathmandu");
        insertUserIfNotExists("Ratna Park User 3", "ratnapark3@yopmail.com", "9800000019", BloodGroup.B_POSITIVE, 27.7065, 85.3135, "Ratna Park, Kathmandu");
    }

    private void insertUserIfNotExists(String fullName, String email, String phoneNumber, BloodGroup bloodGroup, double lat, double lon, String location) {
        if (userRepository.findByEmailId(email).isEmpty()) {
            User user = new User();
            user.setFullName(fullName);
            user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
            user.setRoles(List.of(
                    rolesRepository.findByName(UserRole.USER.toString())
                            .orElseThrow(() -> new RuntimeException("USER Role not found"))
            ));
            user.setEmailId(email);
            user.setPhoneNumber(phoneNumber);
            user.setLocation(location);
            user.setBirthDate(LocalDate.parse("1992-01-01"));
            user.setBloodGroup(bloodGroup);
            user.setLatitude(lat);
            user.setLongitude(lon);
            user.setVerified(true);
            userRepository.save(user);
            log.info("{} inserted.", fullName);
        } else {
            log.info("{} already exists.", fullName);
        }
    }
}
