package org.nurfet.jwtserverspring.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class JwtResponse {

    private final String type = "Bearer";

    private final String role;

    private final String accessToken;

    private final String refreshToken;

}
