package org.nurfet.jwtserverspring.impl;

import org.nurfet.jwtserverspring.exception.NotFoundException;
import org.nurfet.jwtserverspring.jwt.model.RefreshToken;
import org.nurfet.jwtserverspring.model.User;
import org.nurfet.jwtserverspring.repository.UserRepository;
import org.nurfet.jwtserverspring.jwt.service.RefreshTokenService;
import org.nurfet.jwtserverspring.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public UserServiceImpl(UserRepository userRepository,
                           RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsUserByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    @Override
    public Optional<User> findUserByUserName(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public boolean existsUserById(Long id) {
        return userRepository.existsUserById(id);
    }

    @Transactional
    @Override
    public void removeRefreshToken(User user) {

        RefreshToken refreshToken = user.getRefreshToken();

        user.removeRefreshToken();

        userRepository.save(user);

        if (refreshToken != null) {
            refreshTokenService.deleteRefreshTokenById(refreshToken.getId());
        }
    }
}
