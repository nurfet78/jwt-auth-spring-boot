package org.nurfet.jwtserverspring.repository;

import org.nurfet.jwtserverspring.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByAuthority(String authority);
}
