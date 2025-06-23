package com.bibek.bdfs.auth.dto.response;


import com.bibek.bdfs.user.dto.response.RolesResponse;
import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.util.file.FileUrlUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegistrationResponse {
    Long id;
    String fullName;
    String userEmail;
    String phone;
    String location;
    String age;
    String bloodType;
    URI profilePicture;
    List<RolesResponse> userRole;

    public UserRegistrationResponse(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.userEmail = user.getEmailId();
        this.phone = user.getPhoneNumber();
        this.location= user.getLocation();
        this.age= user.getAge();
        this.bloodType= user.getBloodType();
        this.profilePicture = FileUrlUtil.getFileUri(user.getProfileImage());
        this.userRole = user.getRoles().stream()
                .map(RolesResponse::new)
                .toList();
    }
}
