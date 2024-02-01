package org.nurfet.jwtserverspring.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class JwtResponse {

    private final String type = "Bearer";

    private final String role;

    private final String accessToken;

    private final String refreshToken;

    private final String errorMessage;

    public JwtResponse(String role, String accessToken, String refreshToken) {
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.errorMessage = null;
    }

    public JwtResponse(String errorMessage) {
        this.role = null;
        this.accessToken = null;
        this.refreshToken = null;
        this.errorMessage = errorMessage;
    }
}
