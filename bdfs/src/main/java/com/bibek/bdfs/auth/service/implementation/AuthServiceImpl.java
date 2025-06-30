package com.bibek.bdfs.auth.service.implementation;

import com.bibek.bdfs.auth.dto.TokenType;
import com.bibek.bdfs.auth.dto.request.AuthRequest;
import com.bibek.bdfs.auth.dto.request.UserRegistrationRequest;
import com.bibek.bdfs.auth.dto.request.forgot_password.ResetPasswordRequest;
import com.bibek.bdfs.auth.dto.response.AuthResponse;
import com.bibek.bdfs.auth.dto.response.ForgotPasswordResponse;
import com.bibek.bdfs.auth.dto.response.UserRegistrationResponse;
import com.bibek.bdfs.auth.entity.RefreshTokenEntity;
import com.bibek.bdfs.auth.messages.AuthExceptionMessages;
import com.bibek.bdfs.auth.messages.AuthLogMessages;
import com.bibek.bdfs.auth.messages.AuthResponseMessages;
import com.bibek.bdfs.auth.repository.RefreshTokenRepo;
import com.bibek.bdfs.auth.service.AuthService;
import com.bibek.bdfs.security.jwt_auth.JwtTokenGenerator;
import com.bibek.bdfs.user.dto.response.RolesResponse;
import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.user.repository.UserRepository;
import com.bibek.bdfs.user.role.entity.Roles;
import com.bibek.bdfs.user.role.entity.UserRole;
import com.bibek.bdfs.user.role.repository.RolesRepository;
import com.bibek.bdfs.util.file.FileHandlerUtil;
import com.bibek.bdfs.util.file.FileType;
import com.bibek.bdfs.util.validator.EmailValidator;
import com.bibek.bdfs.util.validator.PhoneValidator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepo refreshTokenRepo;
    private final AuthenticationManager authenticationManager;
    private final FileHandlerUtil fileHandlerUtil;
    private final RolesRepository rolesRepository;

    @Override
    public AuthResponse getJwtTokensAfterAuthentication(AuthRequest authenticationRequest, HttpServletResponse response) {
        try {
            // Authenticate the user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.email(),
                            authenticationRequest.password()
                    )
            );

            // Fetch user information after successful authentication
            var userInfoEntity = userRepository.findByEmailId(authenticationRequest.email())
                    .orElseThrow(() -> {
                        log.error(AuthLogMessages.USER_NOT_FOUND, authenticationRequest.email());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");                    });

            // Generate JWT tokens
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            // Save refresh token and create cookie
            saveUserRefreshToken(userInfoEntity, refreshToken);
            createRefreshTokenCookie(response, refreshToken);

            log.info(AuthLogMessages.ACCESS_TOKEN_GENERATED, userInfoEntity.getEmailId());

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userRole(userInfoEntity.getRoles().stream().map(RolesResponse::new).toList())
                    .tokenType(TokenType.Bearer)
                    .build();

        } catch (AuthenticationException e) {
            log.error(AuthLogMessages.INVALID_CREDENTIALS, authenticationRequest.email());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, AuthExceptionMessages.INVALID_CREDENTIALS);
        } catch (Exception e) {
            log.error(AuthLogMessages.EXCEPTION_AUTHENTICATING, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, AuthExceptionMessages.TRY_AGAIN);
        }
    }


    private void saveUserRefreshToken(User user, String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(user)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
    }

    private void createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);
    }

    @Override
    public AuthResponse getAccessTokenUsingRefreshToken(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, AuthExceptionMessages.REFRESH_TOKEN_MISSING);
        }

        // Validate refresh token from database
        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(tokens -> !tokens.isRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, AuthExceptionMessages.REFRESH_TOKEN_REVOKED));

        User user = refreshTokenEntity.getUser();

        // Generate new access token
        Authentication authentication = createAuthenticationObject(user);
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        log.info(AuthLogMessages.ACCESS_TOKEN_GENERATED, user.getEmailId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .tokenType(TokenType.Bearer)
                .build();
    }

    private static Authentication createAuthenticationObject(User user) {
        // Extract user details from UserDetailsEntity
        String username = user.getEmailId();
        String password = user.getPassword();
        List<Roles> roles = user.getRoles();

        // Extract authorities from roles
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> (GrantedAuthority) role::getName)
                .toList();

        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }


    @Override
    public ForgotPasswordResponse forgotPassword(String email) {
        User user = userRepository.findByEmailId(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AuthExceptionMessages.USER_NOT_FOUND + email));

        return new ForgotPasswordResponse(AuthResponseMessages.PASSWORD_RESET_LINK_SENT, user.getEmailId(), LocalDateTime.now());
    }

    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);
        return AuthResponseMessages.PASSWORD_RESET_SUCCESS;
    }

    @Override
    @Transactional
    public UserRegistrationResponse registerUser(UserRegistrationRequest registration) {
        if (!EmailValidator.isValid(registration.userEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AuthExceptionMessages.INVALID_EMAIL);
        }
        if (!PhoneValidator.isValid(registration.phone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AuthExceptionMessages.INVALID_PHONE);
        }
        User user = new User();
        user.setFullName(registration.fullName());
        user.setEmailId(registration.userEmail());
        user.setPhoneNumber(registration.phone());
        user.setPassword(new BCryptPasswordEncoder().encode(registration.password()));
        if (registration.profileImage() == null || registration.profileImage().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile image is required");
        }

        FileType fileType = fileHandlerUtil.determineFileType(Objects.requireNonNull(registration.profileImage().getOriginalFilename()));
        if (fileType == FileType.IMAGE) {
            String fileName = fileHandlerUtil.saveFile(registration.profileImage(), registration.userEmail()).getFileDownloadUri();
            user.setProfileImage(fileName);
            Roles role = rolesRepository.findByName(UserRole.USER.name()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
            user.setRoles(List.of(role));
            user.setBirthDate(registration.birthDate());
            user.setBloodGroup(registration.bloodGroup());
            user.setLatitude(registration.latitude());
            user.setLongitude(registration.longitude());
            user.setVerified(true);
            userRepository.save(user);
            return new UserRegistrationResponse(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type");
        }
    }
}