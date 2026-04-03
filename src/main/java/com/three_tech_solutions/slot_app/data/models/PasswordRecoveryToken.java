package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.*;
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
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private int token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean disabled;

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }
}