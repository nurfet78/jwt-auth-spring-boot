package org.nurfet.jwtserverspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Requested item not found")
public class UserNotFoundException extends RuntimeException{
    public <T> UserNotFoundException(Class<T> cls, String username) {
        super(cls.getSimpleName() + " " + String.format("User with username: %s does not exists!", username));
    }
}
