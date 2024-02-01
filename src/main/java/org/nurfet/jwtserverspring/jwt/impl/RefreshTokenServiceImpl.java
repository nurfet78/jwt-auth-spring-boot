package org.nurfet.jwtserverspring.jwt.impl;

import org.nurfet.jwtserverspring.exception.UserNotFoundException;
import org.nurfet.jwtserverspring.jwt.config.JwtConfig;
import org.nurfet.jwtserverspring.jwt.model.RefreshToken;
import org.nurfet.jwtserverspring.model.User;
import org.nurfet.jwtserverspring.jwt.repository.RefreshTokenRepository;
import org.nurfet.jwtserverspring.repository.UserRepository;
import org.nurfet.jwtserverspring.jwt.service.RefreshTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtConfig jwtConfig;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenServiceImpl(JwtConfig jwtConfig,
                                   RefreshTokenRepository refreshTokenRepository,
                                   UserRepository userRepository) {
        this.jwtConfig = jwtConfig;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }
    @Override
    public void saveRefreshToken(String username, String refreshToken) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            RefreshToken existingToken = user.getRefreshToken();

            final LocalDateTime now = LocalDateTime.now();
            final Instant issuedAtInstant = now.atZone(ZoneId.systemDefault()).toInstant();
            final Instant expirationTime = now.plusMinutes(jwtConfig.getRefreshTokenExpiration()).atZone(ZoneId.systemDefault()).toInstant();

            if (existingToken != null) {
                // Обновляем существующий refresh-токен
                existingToken.setToken(refreshToken);
                existingToken.setExpirationTime(expirationTime);
                existingToken.setCreationTime(issuedAtInstant);
                existingToken.setUsername(username);
                user.setRefreshToken(existingToken);
                refreshTokenRepository.save(existingToken);
            } else {
                // Создаем и сохраняем новый refresh-токен
                RefreshToken newToken = new RefreshToken(username, refreshToken, expirationTime, issuedAtInstant, user);
                user.setRefreshToken(newToken);
                refreshTokenRepository.save(newToken);
            }
        } else {
            throw new UserNotFoundException(RefreshToken.class, username);
        }
    }

    @Override
    public String findRefreshTokenByUsername(String username) {
        return refreshTokenRepository.findRefreshTokenByUsername(username)
                .map(RefreshToken::getToken).orElse(null);
    }

    @Transactional
    @Override
    public void deleteRefreshTokenById(Long id) {
        refreshTokenRepository.deleteById(id);
    }
}
