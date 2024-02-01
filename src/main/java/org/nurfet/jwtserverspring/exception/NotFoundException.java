package org.nurfet.jwtserverspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Requested item not found")
public class NotFoundException extends RuntimeException{
    public <T> NotFoundException(Class<T> cls, Long id) {
        super(cls.getSimpleName() + " " + String.format("with id: %d does not exists!", id));
    }
}
