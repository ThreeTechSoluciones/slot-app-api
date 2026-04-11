package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.ValidateTokenRequest;
import com.three_tech_solutions.slot_app.data.models.PasswordRecoveryToken;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.PasswordRecoveryTokenRepository;
import com.three_tech_solutions.slot_app.services.interfaces.PasswordRecoveryTokenService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class PasswordRecoveryTokenServiceImpl implements PasswordRecoveryTokenService {

    private final PasswordRecoveryTokenRepository passwordRecoverytokenRepository;

    public PasswordRecoveryTokenServiceImpl(PasswordRecoveryTokenRepository passwordRecoverytokenRepository) {
        this.passwordRecoverytokenRepository = passwordRecoverytokenRepository;
    }

    @Override
    public void validateTokenAndDisableIt(User user, int token) {

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
    public void validateToken(ValidateTokenRequest validateTokenRequest) {
        this.passwordRecoverytokenRepository.findByToken(123);
    }
}
