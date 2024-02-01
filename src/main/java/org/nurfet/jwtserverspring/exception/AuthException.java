package org.nurfet.jwtserverspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Requested item bad")
public class AuthException extends RuntimeException{
    public AuthException(String message) {
        super(message);
    }
}
