package org.nurfet.jwtserverspring.config;

import org.nurfet.jwtserverspring.model.Role;
import org.nurfet.jwtserverspring.model.User;
import org.nurfet.jwtserverspring.repository.UserRepository;
import org.nurfet.jwtserverspring.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class InitDB implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public InitDB(UserRepository userRepository,
                  RoleService roleService,
                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        addDefaultUser();
    }


    private void addDefaultUser() {
        userRepository.findUserByUsername("admin").orElseGet(() -> {
            Role roleAdmin = roleService.addRole("ROLE_USER");
            Role roleUser = roleService.addRole("ROLE_ADMIN");

            User user = new User();
            user.setRoles(new HashSet<>(Arrays.asList(roleAdmin, roleUser)));
            user.setFirstName("Александр");
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin"));


            return userRepository.save(user);
        });
    }
}
