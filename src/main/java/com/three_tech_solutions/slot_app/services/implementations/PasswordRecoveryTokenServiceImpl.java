package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.data.models.PasswordRecoveryToken;
import com.three_tech_solutions.slot_app.data.repositories.PasswordRecoveryTokenRepository;
import com.three_tech_solutions.slot_app.data.repositories.PaymentRepository;
import com.three_tech_solutions.slot_app.services.interfaces.MonthlyFeeService;
import com.three_tech_solutions.slot_app.services.interfaces.PasswordRecoveryTokenService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class PasswordRecoveryTokenServiceImpl implements PasswordRecoveryTokenService {

    private final PasswordRecoveryTokenRepository passwordRecoverytokenRepository;

    public PasswordRecoveryTokenServiceImpl(PasswordRecoveryTokenRepository passwordRecoverytokenRepository) {
        this.passwordRecoverytokenRepository = passwordRecoverytokenRepository;
    }

    @Override
    public PasswordRecoveryToken getValidToken(String username, int token) {

        PasswordRecoveryToken tokenEntity = passwordRecoverytokenRepository
                .findByUsernameAndToken(username, token)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Token inválido"));

        if (tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(BAD_REQUEST, "Token expirado");
        }

        return tokenEntity;
    }

    @Override
    public void deleteToken(PasswordRecoveryToken token) {
        passwordRecoverytokenRepository.delete(token);
    }
}
