package org.nurfet.jwtserverspring.jwt.service;

public interface RefreshTokenService {
    void saveRefreshToken(String username, String refreshToken);
    String findRefreshTokenByUsername(String username);

    void deleteRefreshTokenById(Long id);
}
