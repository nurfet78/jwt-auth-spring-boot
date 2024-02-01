package org.nurfet.jwtserverspring.controller;

import lombok.RequiredArgsConstructor;
import org.nurfet.jwtserverspring.dto.UserDto;
import org.nurfet.jwtserverspring.jwt.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegistrationController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {

        authService.register(userDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("Message", "User successfully registered"));
    }
}
