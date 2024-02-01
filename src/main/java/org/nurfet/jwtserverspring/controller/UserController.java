package org.nurfet.jwtserverspring.controller;

import lombok.RequiredArgsConstructor;
import org.nurfet.jwtserverspring.dto.UserDto;
import org.nurfet.jwtserverspring.model.User;
import org.nurfet.jwtserverspring.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        List<UserDto> userDtos;
        userDtos = users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);

        UserDto userDto = new UserDto(user);

        return ResponseEntity.ok(userDto);
    }
}
