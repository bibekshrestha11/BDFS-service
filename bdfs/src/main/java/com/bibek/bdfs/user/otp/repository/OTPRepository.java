package com.bibek.bdfs.user.otp.repository;

import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.user.otp.entity.OTP;
import com.bibek.bdfs.user.otp.entity.OTPPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findByUserAndOtpValue(User user, String otp);

    Optional<OTP> findByOtpValueAndPurpose(String otp, OTPPurpose purpose);

    Optional<OTP> findByUserAndOtpValueAndPurpose(User user, String otpValue, OTPPurpose purpose);
}
