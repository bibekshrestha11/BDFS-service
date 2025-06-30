package com.bibek.bdfs.auth.controller;

import com.bibek.bdfs.auth.dto.request.AuthRequest;
import com.bibek.bdfs.auth.dto.request.UserRegistrationRequest;
import com.bibek.bdfs.auth.dto.request.forgot_password.ResetPasswordRequest;
import com.bibek.bdfs.auth.dto.response.AuthResponse;
import com.bibek.bdfs.auth.dto.response.ForgotPasswordResponse;
import com.bibek.bdfs.auth.dto.response.UserRegistrationResponse;
import com.bibek.bdfs.auth.messages.AuthResponseMessages;
import com.bibek.bdfs.auth.service.AuthService;
import com.bibek.bdfs.common.BaseController;
import com.bibek.bdfs.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "APIs for user authentication and password management")
public class AuthController extends BaseController {

    private final AuthService authService;

    @Operation(
            summary = "User login",
            description = "Authenticates a user using email and password, then returns JWT access and refresh tokens."
    )
    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticateUser(
            @RequestBody AuthRequest authentication, HttpServletResponse response) {

        log.info("[AuthController:authenticateUser] User: {} is trying to authenticate", authentication.email());
        return successResponse(authService.getJwtTokensAfterAuthentication(authentication, response),
                AuthResponseMessages.USER_AUTHENTICATED);
    }

    @Operation(
            summary = "Refresh JWT access token",
            description = "Generates a new access token using a valid refresh token."
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> getAccessToken(
            @CookieValue(name = "refresh_token", required = false)
            @Parameter(description = "Refresh token stored in cookies") String refreshToken) {

        return successResponse(authService.getAccessTokenUsingRefreshToken(refreshToken),
                AuthResponseMessages.ACCESS_TOKEN_REFRESHED);
    }

    @Operation(
            summary = "User registration",
            description = "Registers a new user with the provided details."
    )
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<UserRegistrationResponse>> registerUser(
            @ModelAttribute UserRegistrationRequest registration) {

        log.info("[AuthController:registerUser] User: {} is trying to register", registration.userEmail());
        return successResponse(authService.registerUser(registration),
                AuthResponseMessages.USER_REGISTERED);
    }

    @PostMapping("/verify-email")
    @Operation(
            summary = "Verify email",
            description = "Verifies the user's email using the provided token and email."
    )
    public ResponseEntity<ApiResponse<String>> verifyEmail(
            @RequestParam
            @Parameter(description = "Email address of the user", required = true, example = "user@openmichub.com") String email,
            @RequestParam
            @Parameter(description = "Verification token sent to the user's email", required = true, example = "123456") String token) {
        return successResponse(authService.verifyEmail(email, token),
                AuthResponseMessages.EMAIL_VERIFIED);
    }

    //resend verification email
    @Operation(
            summary = "Resend verification email",
            description = "Resends the verification email to the user."
    )
    @PostMapping("/resend-verification-email")
    public ResponseEntity<ApiResponse<UserRegistrationResponse>> resendVerificationEmail(
            @RequestParam
            @Parameter(description = "User's email address", required = true, example = "user@openmichub.com") String email) {
        log.info("[AuthController:resendVerificationEmail] Resending verification email to: {}", email);
        return successResponse(authService.resendVerificationEmail(email),
                AuthResponseMessages.VERIFICATION_EMAIL_RESENT);
    }

    @Operation(
            summary = "Forgot password",
            description = "Sends a password reset link to the provided recipientEmail."
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<ForgotPasswordResponse>> forgotPassword(
            @RequestParam
            @Parameter(description = "Email address of the user", required = true, example = "admin@openmichub.com") String email) {

        return successResponse(authService.forgotPassword(email),
                AuthResponseMessages.PASSWORD_RESET_LINK_SENT);
    }

    @Operation(
            summary = "Reset password",
            description = "Resets the user's password using the token received via recipientEmail."
    )
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return successResponse(authService.resetPassword(resetPasswordRequest),
                AuthResponseMessages.PASSWORD_RESET_SUCCESSFULLY);
    }



}
