package org.nurfet.jwtserverspring.impl;

import org.nurfet.jwtserverspring.model.Role;
import org.nurfet.jwtserverspring.repository.RoleRepository;
import org.nurfet.jwtserverspring.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> findRoleByAuthority(String authority) {
        return roleRepository.findRoleByAuthority(authority);
    }

    @Override
    public Role addRole(String roleName) {
        return roleRepository.findRoleByAuthority(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
    }
}
