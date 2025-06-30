package com.bibek.bdfs.user.dto.request;

import com.bibek.bdfs.user.entity.BloodGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotEmpty(message = "Full name must not be empty")
    String fullName;
    @NotEmpty(message = "Phone number must not be empty")
    String phone;
    MultipartFile profileImage;
    LocalDate birthDate;
    @NotEmpty(message = "Blood group is required")
    BloodGroup bloodGroup;
    String location;
    Double latitude;
    Double longitude;
}
