package com.bibek.bdfs.blood_request.dto;

import com.bibek.bdfs.user.entity.BloodGroup;
import com.bibek.bdfs.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BloodRequestUserResponse {
    private String fullName;
    private String emailId;
    private String profileImage;
    private String phoneNumber;
    private BloodGroup bloodGroup;

    public BloodRequestUserResponse(User user){
        this.fullName = user.getFullName();
        this.emailId = user.getEmailId();
        this.profileImage = user.getProfileImage();
        this.phoneNumber = user.getPhoneNumber();
        this.bloodGroup = user.getBloodGroup();
    }
}
