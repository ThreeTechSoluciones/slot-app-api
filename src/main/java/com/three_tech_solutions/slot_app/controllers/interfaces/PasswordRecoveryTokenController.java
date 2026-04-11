package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.ValidateTokenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/password-recovery-tokens")
public interface PasswordRecoveryTokenController {
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/validate")
    void validateTokenAndDisableIt(@RequestBody ValidateTokenRequest request);
}
