package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.PasswordRecoveryToken;

public interface PasswordRecoveryTokenService {
    PasswordRecoveryToken getValidToken(String username, int token);
    void deleteToken(PasswordRecoveryToken token);
}
