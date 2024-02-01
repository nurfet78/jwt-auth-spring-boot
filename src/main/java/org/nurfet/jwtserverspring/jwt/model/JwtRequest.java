package org.nurfet.jwtserverspring.jwt.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequest {

    private String username;

    private String password;
}
