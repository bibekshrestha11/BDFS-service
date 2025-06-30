package com.bibek.bdfs.auth.dto.response;


import com.bibek.bdfs.user.dto.response.RolesResponse;
import com.bibek.bdfs.user.entity.BloodGroup;
import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.util.file.FileUrlUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegistrationResponse {
    Long id;
    String fullName;
    String userEmail;
    String phone;
    URI profilePicture;
    List<RolesResponse> userRole;
    LocalDate birthDate;
    BloodGroup bloodGroup;
    String location;
    Double latitude;
    Double longitude;
    LocalDateTime otpExpiryTime;


    public UserRegistrationResponse(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.userEmail = user.getEmailId();
        this.phone = user.getPhoneNumber();
        this.profilePicture = FileUrlUtil.getFileUri(user.getProfileImage());
        this.userRole = user.getRoles().stream()
                .map(RolesResponse::new)
                .toList();
        this.birthDate = user.getBirthDate();
        this.bloodGroup = user.getBloodGroup();
        this.location = user.getLocation();
        this.latitude = user.getLatitude();
        this.longitude = user.getLongitude();
    }

    public UserRegistrationResponse(User user, LocalDateTime otpExpiryTime) {
        this(user);
        this.otpExpiryTime = otpExpiryTime;
    }
}
