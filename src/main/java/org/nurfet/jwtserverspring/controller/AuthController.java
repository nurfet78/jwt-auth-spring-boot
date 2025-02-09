package org.nurfet.jwtserverspring.controller;

import lombok.RequiredArgsConstructor;
import org.nurfet.jwtserverspring.jwt.model.JwtRequest;
import org.nurfet.jwtserverspring.jwt.model.JwtResponse;
import org.nurfet.jwtserverspring.jwt.model.RefreshJwtRequest;
import org.nurfet.jwtserverspring.jwt.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> loginRequest(@RequestBody JwtRequest authRequest) {
        final JwtResponse token = authService.login(authRequest);
        if(token == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "JWT токен отозван");
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        if(token == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "JWT токен отозван");
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        if(token == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "JWT токен отозван");
        }
        return ResponseEntity.ok(token);
    }
}
