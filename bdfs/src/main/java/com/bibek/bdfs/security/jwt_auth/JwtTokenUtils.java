package com.bibek.bdfs.security.jwt_auth;

import com.bibek.bdfs.config.user.UserInfoConfig;
import com.bibek.bdfs.user.repository.UserRepository;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    private final UserRepository userRepository;

    public String getUserName(JWTClaimsSet claims) {
        return claims.getSubject();
    }

    public boolean isTokenValid(JWTClaimsSet claims, UserDetails userDetails) {
        final String userName = getUserName(claims);
        boolean isTokenExpired = getIfTokenIsExpired(claims);
        boolean isTokenUserSameAsDatabase = userName.equals(userDetails.getUsername());
        return !isTokenExpired && isTokenUserSameAsDatabase;
    }

    private boolean getIfTokenIsExpired(JWTClaimsSet claims) {
        Date expiration = claims.getExpirationTime();
        return expiration != null && expiration.before(Date.from(Instant.now()));
    }

    public UserDetails userDetails(String emailId) {
        return userRepository
                .findByEmailId(emailId)
                .map(UserInfoConfig::new)
                .orElseThrow(() -> new UsernameNotFoundException("UserEmail: " + emailId + " does not exist"));
    }
}