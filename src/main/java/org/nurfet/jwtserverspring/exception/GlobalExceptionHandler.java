package org.nurfet.jwtserverspring.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleNotFound(HttpServletRequest request, NotFoundException e) {
        ApiError response = new ApiError(HttpStatus.NOT_FOUND.value(), request.getRequestURL().toString(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                        .withZone(ZoneId.systemDefault()).format(Instant.now()),
                e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Message", response));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> NotFound(HttpServletRequest request, UserNotFoundException e) {
        ApiError response = new ApiError(HttpStatus.NOT_FOUND.value(), request.getRequestURL().toString(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                        .withZone(ZoneId.systemDefault()).format(Instant.now()),
                e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Message", response));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> forbidden(HttpServletRequest request, AccessDeniedException e) {
        ApiError response = new ApiError(HttpStatus.NOT_FOUND.value(), request.getRequestURL().toString(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                        .withZone(ZoneId.systemDefault()).format(Instant.now()),
                e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("Message", response));
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> authHandle(HttpServletRequest request, AuthException e) {
        ApiError response = new ApiError(HttpStatus.UNAUTHORIZED.value(), request.getRequestURL().toString(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                        .withZone(ZoneId.systemDefault()).format(Instant.now()),
                e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", response));
    }
}
