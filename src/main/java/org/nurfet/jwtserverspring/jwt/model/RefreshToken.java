package org.nurfet.jwtserverspring.jwt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nurfet.jwtserverspring.model.AbstractEntity;
import org.nurfet.jwtserverspring.model.User;

import java.io.Serial;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends AbstractEntity<Long> {

    @Serial
    private static final long serialVersionUID = 4L;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private Instant creationTime;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private Instant expirationTime;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
