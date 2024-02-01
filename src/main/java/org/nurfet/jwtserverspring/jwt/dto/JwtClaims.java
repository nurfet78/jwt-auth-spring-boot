package org.nurfet.jwtserverspring.jwt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;



@Getter
@RequiredArgsConstructor(staticName = "of")
public final class JwtClaims {

    private final String roles;

    private final Boolean isRefreshToken;
}
