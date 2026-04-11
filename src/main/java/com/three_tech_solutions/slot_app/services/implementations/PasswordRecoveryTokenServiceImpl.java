package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.ValidateTokenRequest;
import com.three_tech_solutions.slot_app.data.models.PasswordRecoveryToken;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.PasswordRecoveryTokenRepository;
import com.three_tech_solutions.slot_app.services.interfaces.PasswordRecoveryTokenService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class PasswordRecoveryTokenServiceImpl implements PasswordRecoveryTokenService {

    private final PasswordRecoveryTokenRepository passwordRecoverytokenRepository;
    private final long TOKEN_EXPIRATION_MINUTES = 10L;

    public PasswordRecoveryTokenServiceImpl(PasswordRecoveryTokenRepository passwordRecoverytokenRepository) {
        this.passwordRecoverytokenRepository = passwordRecoverytokenRepository;
    }

    @Override
    public void validateTokenAndDisableIt(User user, String token) {

        PasswordRecoveryToken tokenEntity = passwordRecoverytokenRepository
                .findByUserAndToken(user, token)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Token inválido"));

        if (tokenEntity.isExpired()) {
            throw new ResponseStatusException(BAD_REQUEST, "Token expirado");
        }

        if (tokenEntity.isDisabled()) {
            throw new ResponseStatusException(BAD_REQUEST, "Token ya utilizado");
        }

        tokenEntity.setDisabled(true);
        passwordRecoverytokenRepository.save(tokenEntity);
    }

    @Override
    public void savePasswordRecoveryTokenForUser(User user, String code) {
        deactivateTokenIfUserAlreadyHasOne(user);
        saveNewToken(user, code);
    }

    @Override
    public void validateToken(ValidateTokenRequest validateTokenRequest) {
        this.passwordRecoverytokenRepository
                .findByToken(validateTokenRequest.token())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "El token ingreso es inválido."));
    }


    private void saveNewToken(User user, String code) {
        passwordRecoverytokenRepository.save(new PasswordRecoveryToken(
                user, code, LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_MINUTES)
        ));
    }

    private void deactivateTokenIfUserAlreadyHasOne(User user) {
        passwordRecoverytokenRepository
                .findByUserAndDisabledFalse(user)
                .ifPresent(token -> {
                    token.setDisabled(true);
                    passwordRecoverytokenRepository.save(token);
                });
    }
}
