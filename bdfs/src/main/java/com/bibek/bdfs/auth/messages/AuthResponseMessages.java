package com.bibek.bdfs.auth.messages;

public class AuthResponseMessages {

    public static final String USER_REGISTERED = "User registered successfully";
    public static final String EMAIL_VERIFIED = "Email verified successfully";
    public static final String VERIFICATION_EMAIL_RESENT = "Verification email resent successfully";
    public static final String EMAIL_ALREADY_VERIFIED = "Email is already verified";

    private AuthResponseMessages() {}
    public static final String USER_AUTHENTICATED = "User authenticated successfully";
    public static final String ACCESS_TOKEN_REFRESHED = "Access token refreshed successfully";
    public static final String PASSWORD_RESET_LINK_SENT = "Password reset link sent to your email";
    public static final String PASSWORD_RESET_SUCCESSFULLY = "Password reset successfully";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successfully";
}
