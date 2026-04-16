package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRecoveryToken {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String token;
    private LocalDateTime expiresAt;
    @Id
    private UUID id = UUID.randomUUID();
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean disabled = false;

    public PasswordRecoveryToken(User user, String token, LocalDateTime expiresAt) {
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }
}