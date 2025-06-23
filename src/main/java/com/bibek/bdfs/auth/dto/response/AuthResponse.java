package com.bibek.bdfs.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.bibek.bdfs.auth.dto.TokenType;
import com.bibek.bdfs.user.dto.response.RolesResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("access_token_expiry")
    private int accessTokenExpiry;

    @JsonProperty("token_type")
    private TokenType tokenType;

    @JsonProperty("user_role")
    private List<RolesResponse> userRole;

}