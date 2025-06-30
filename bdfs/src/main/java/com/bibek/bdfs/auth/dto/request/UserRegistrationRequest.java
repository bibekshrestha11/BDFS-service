package com.bibek.bdfs.auth.dto.request;

import com.bibek.bdfs.user.entity.BloodGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record UserRegistrationRequest(
        @NotEmpty(message = "Full name must not be empty")
        String fullName,
        @NotEmpty(message = "User email must not be empty") // Neither null nor 0 size
        @Email(message = "Invalid email format")
        String userEmail,
        @NotEmpty(message = "Phone number must not be empty")
        String phone,
        @NotEmpty(message = "Password must not be empty")
        String password,
        MultipartFile profileImage,
        LocalDate birthDate,
        @NotEmpty(message = "Blood group is required")
        BloodGroup bloodGroup,
        String location,
        Double latitude,
        Double longitude
) { }