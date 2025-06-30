package com.bibek.bdfs.user.otp.service;


import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.user.otp.entity.OTP;
import com.bibek.bdfs.user.otp.entity.OTPPurpose;

public interface OTPService {
    OTP saveOTP(User user, OTPPurpose purpose);

    void validateOTP(User user, String otp, OTPPurpose purpose);

    OTP getOTP(String otp, OTPPurpose purpose);
}
