package org.nurfet.jwtserverspring.exception;


public class CustomJwtException extends RuntimeException{

    public CustomJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
