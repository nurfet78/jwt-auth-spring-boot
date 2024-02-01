package org.nurfet.jwtserverspring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Role extends AbstractEntity<Long> implements GrantedAuthority {
    @Serial
    private static final long serialVersionUID = 2L;
    @Column(nullable = false)
    private String authority;
    public Role(String authority) {
        this.authority = authority;
    }

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    @Override
    public String getAuthority() {
        return authority;
    }
}
