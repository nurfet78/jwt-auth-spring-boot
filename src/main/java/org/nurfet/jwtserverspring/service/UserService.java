package org.nurfet.jwtserverspring.service;

import org.nurfet.jwtserverspring.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User findUserById(Long id);
    User saveUser(User user);
    void deleteUserById(Long id);
    boolean existsUserByUsername(String username);
    Optional<User> findUserByUserName(String username);
    boolean existsUserById(Long id);
    void removeRefreshToken(User user);
}
