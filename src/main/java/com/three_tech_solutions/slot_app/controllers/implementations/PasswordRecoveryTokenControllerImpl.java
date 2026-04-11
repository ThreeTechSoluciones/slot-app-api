package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.PasswordRecoveryTokenController;
import com.three_tech_solutions.slot_app.controllers.requests.ValidateTokenRequest;
import com.three_tech_solutions.slot_app.services.interfaces.PasswordRecoveryTokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class PasswordRecoveryTokenControllerImpl implements PasswordRecoveryTokenController {

    private final PasswordRecoveryTokenService passwordRecoveryTokenService;

    @Override
    public void validateTokenAndDisableIt(ValidateTokenRequest request) {
        this.passwordRecoveryTokenService.validateToken(request);
    }
}
