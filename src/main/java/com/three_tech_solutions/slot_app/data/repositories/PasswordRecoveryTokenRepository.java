package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.PasswordRecoveryToken;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, UUID> {

    Optional<PasswordRecoveryToken> findByUserAndToken(User user, String token);

    Optional<PasswordRecoveryToken> findByUserAndDisabledFalse(User user);

    Optional<PasswordRecoveryToken> findByToken(String token);
}
