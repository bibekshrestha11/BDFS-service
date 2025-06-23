package com.bibek.bdfs.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public record UserRegistrationRequest(
        @NotEmpty(message = "Full name must not be empty")
        String fullName,
        @NotEmpty(message = "User email must not be empty") // Neither null nor 0 size
        @Email(message = "Invalid email format")
        String userEmail,
        @NotEmpty(message = "Phone number must not be empty")
        String phone,
        @NotEmpty(message = "Location must not be empty")
        String location,
        @NotEmpty(message = "Blood type must not be empty")
        String bloodType,

        @NotEmpty(message = "Age  must not be empty")
        String age,

        @NotEmpty(message = "Password must not be empty")
        String password,
        MultipartFile profileImage,
        Long roleId
        //profession, profile,
) { }