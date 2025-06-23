package com.bibek.bdfs.auth.service;

import com.bibek.bdfs.auth.dto.request.AuthRequest;
import com.bibek.bdfs.auth.dto.request.UserRegistrationRequest;
import com.bibek.bdfs.auth.dto.request.forgot_password.ResetPasswordRequest;
import com.bibek.bdfs.auth.dto.response.AuthResponse;
import com.bibek.bdfs.auth.dto.response.ForgotPasswordResponse;
import com.bibek.bdfs.auth.dto.response.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;

public interface AuthService {


    AuthResponse getJwtTokensAfterAuthentication(AuthRequest authenticationRequest, HttpServletResponse response);

    AuthResponse getAccessTokenUsingRefreshToken(@CookieValue(name = "refresh_token", required = false) String refreshToken);

    ForgotPasswordResponse forgotPassword(String email);

    String resetPassword(ResetPasswordRequest resetPasswordRequest);

    UserRegistrationResponse registerUser(UserRegistrationRequest registration);
}