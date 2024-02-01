package org.nurfet.jwtserverspring.service;

import org.nurfet.jwtserverspring.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRoles();
    Role saveRole(Role role);
    Optional<Role> findRoleByAuthority(String authority);
    Role addRole(String roleName);
}
