package com.bibek.bdfs.security.jwt_auth;

import com.bibek.bdfs.config.RSAKeyRecord;
import com.bibek.bdfs.security.WHITE_LIST_URLS;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Enumeration;

@RequiredArgsConstructor
@Slf4j
public class JwtAccessTokenFilter extends OncePerRequestFilter {

    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logRequestDetails(request);

        String requestURI = request.getRequestURI();
        if (isWhitelisted(requestURI)) {
            log.info("Skipping JWT filter for: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("[JwtAccessTokenFilter] Filtering request: {}", requestURI);

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("[JwtAccessTokenFilter] Invalid or missing Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            // Decrypt the JWE
            JWEObject jweObject = JWEObject.parse(token);
            RSADecrypter decrypter = new RSADecrypter(rsaKeyRecord.rsaPrivateKey());
            jweObject.decrypt(decrypter);

            // Get claims from decrypted payload
            JWTClaimsSet claims = JWTClaimsSet.parse(jweObject.getPayload().toJSONObject());

            // Validate and authenticate
            final String userName = jwtTokenUtils.getUserName(claims);
            if (!userName.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = jwtTokenUtils.userDetails(userName);
                if (jwtTokenUtils.isTokenValid(claims, userDetails)) {
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken createdToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(createdToken);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
        } catch (ParseException | JOSEException e) {
            log.error("[JwtAccessTokenFilter] Error processing token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private boolean isWhitelisted(String requestURI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return Arrays.stream(WHITE_LIST_URLS.values())
                .map(WHITE_LIST_URLS::getUrl)
                .anyMatch(url -> pathMatcher.match(url, requestURI));
    }


    private void logRequestDetails(HttpServletRequest request) {
        log.info("Request URI: {}", request.getRequestURI());
        log.info("HTTP Method: {}", request.getMethod());
        log.info("Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.info("{}: {}", headerName, headerValue);
        }
    }
}