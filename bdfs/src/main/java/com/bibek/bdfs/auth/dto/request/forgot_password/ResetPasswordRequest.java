package com.bibek.bdfs.auth.dto.request.forgot_password;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    private String otp;
    private String newPassword;
}
