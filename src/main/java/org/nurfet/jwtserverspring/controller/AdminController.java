package org.nurfet.jwtserverspring.controller;

import lombok.RequiredArgsConstructor;
import org.nurfet.jwtserverspring.dto.UserDto;
import org.nurfet.jwtserverspring.model.User;
import org.nurfet.jwtserverspring.jwt.service.AuthService;
import org.nurfet.jwtserverspring.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    private final AuthService authService;

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserDto updatedUserDto) {
        if (!userService.existsUserById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Message", String.format("User with ID %d not found", userId)));
        }

        User existingUser = userService.findUserById(userId);

        String newUsername = updatedUserDto.getUsername();
        // Проверка, изменено ли имя пользователя
        if (!existingUser.getUsername().equals(newUsername)) {
            // Если имя пользователя изменено, проверяем, существует ли пользователь с таким именем
            if (userService.existsUserByUsername(newUsername)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("Message", String.format("Username '%s' already exists", newUsername)));
            }
        }

        authService.updateUserAndTokens(existingUser, updatedUserDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("Message", "User updated successfully"));
    }


    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (!userService.existsUserById(userId)) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Message", String.format("User with ID %d not found", userId)));
        }

        userService.deleteUserById(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("Message", "User deleted successfully"));
    }

    @DeleteMapping("/revokeRefreshToken/{userId}")
    public ResponseEntity<?> revokeRefreshToken(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        try {
            userService.removeRefreshToken(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("Message",
                            String.format("Refresh token of user %s revoked", user.getUsername())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(String.format("Failed to revoke %s user token", user.getUsername()));
        }
    }
}
