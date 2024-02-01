package org.nurfet.jwtserverspring.jwt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String accessSecret;

    private String refreshSecret;

    private int accessTokenExpiration;

    private int refreshTokenExpiration;
}
