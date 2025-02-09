package org.nurfet.jwtserverspring.jwt.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.nurfet.jwtserverspring.dto.UserDto;
import org.nurfet.jwtserverspring.exception.AuthException;
import org.nurfet.jwtserverspring.exception.CustomJwtException;
import org.nurfet.jwtserverspring.exception.NotFoundException;
import org.nurfet.jwtserverspring.jwt.provider.JwtProvider;
import org.nurfet.jwtserverspring.jwt.model.JwtResponse;
import org.nurfet.jwtserverspring.model.User;
import org.nurfet.jwtserverspring.jwt.model.JwtRequest;
import org.nurfet.jwtserverspring.jwt.model.RefreshToken;
import org.nurfet.jwtserverspring.model.Role;
import org.nurfet.jwtserverspring.exception.UserNotFoundException;
import org.nurfet.jwtserverspring.service.RoleService;
import org.nurfet.jwtserverspring.service.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final RefreshTokenService refreshTokenService;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    public JwtResponse login(JwtRequest authRequest) {
        final User user = userService.findUserByUserName(authRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException(AuthService.class, authRequest.getUsername()));

        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            return getJwtResponse(user);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    private JwtResponse getJwtResponse(User user) {
        final String accessToken = jwtProvider.generateToken(user, false);
        final String refreshToken = jwtProvider.generateToken(user, true);
        final String role = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        refreshTokenService.saveRefreshToken(user.getUsername(), refreshToken);
        return new JwtResponse(role, accessToken, refreshToken);
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshTokenService.findRefreshTokenByUsername(username);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.findUserByUserName(username)
                        .orElseThrow(() -> new UserNotFoundException(AuthService.class, username));
                final String accessToken = jwtProvider.generateToken(user, false);
                final String role = user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(", "));

                return new JwtResponse(role, accessToken, null);
            }
        }
        return null;
    }

    public JwtResponse refresh(String refreshToken){

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshTokenService.findRefreshTokenByUsername(username);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.findUserByUserName(username)
                        .orElseThrow(() -> new UserNotFoundException(AuthService.class, username));

                return getJwtResponse(user);
            }
        }

        return null;
    }
    public void updateUserAndTokens(User existingUser, UserDto updatedUserDto) {
        existingUser.setFirstName(updatedUserDto.getFirstName());
        existingUser.setUsername(updatedUserDto.getUsername());
        existingUser.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));

        userService.saveUser(existingUser);

        RefreshToken existingRefreshToken = existingUser.getRefreshToken();

        if (existingRefreshToken != null) {
            String newRefreshTokenValue = jwtProvider.generateToken(existingUser, true);

            refreshTokenService.saveRefreshToken(existingUser.getUsername(), newRefreshTokenValue);
        }
    }
    public void register(UserDto userDto) {

        if (userService.existsUserByUsername(userDto.getUsername())) {
            throw new AuthException(String.format("Username %s already exists", userDto.getUsername()));
        } else {
            User newUser = new User();
            newUser.setFirstName(userDto.getFirstName());
            newUser.setUsername(userDto.getUsername());
            newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

            Role roleUser = roleService.addRole("ROLE_USER");

            newUser.setRoles(Collections.singleton(roleUser));

            userService.saveUser(newUser);
        }
    }
}
