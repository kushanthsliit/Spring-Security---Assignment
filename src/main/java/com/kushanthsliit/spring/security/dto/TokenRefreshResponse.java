package com.kushanthsliit.spring.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshResponse {

    private String tokenType;
    private String refreshToken;
    private String accessToken;

}
