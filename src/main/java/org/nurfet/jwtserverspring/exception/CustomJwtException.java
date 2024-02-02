package org.nurfet.jwtserverspring.exception;

import lombok.Getter;

@Getter
public class CustomJwtException extends RuntimeException{

    private final String originalException;
    public CustomJwtException(String message, Throwable originalException) {
        super(message, originalException);
        this.originalException = originalException.getMessage();
    }
}
