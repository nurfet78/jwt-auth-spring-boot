package org.nurfet.jwtserverspring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nurfet.jwtserverspring.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String firstName;

    private String username;

    private String password;

    private Set<String> roles;
    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.username = user.getUsername();
        this.roles = user.getAuthorities().stream()
                .map((GrantedAuthority authority) -> authority.getAuthority())
                .collect(Collectors.toSet());
    }
}
