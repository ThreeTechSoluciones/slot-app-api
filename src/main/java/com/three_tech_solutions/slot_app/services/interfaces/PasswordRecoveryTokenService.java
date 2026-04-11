package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.User;

public interface PasswordRecoveryTokenService {
    void validateTokenAndDisableIt(User user, String token);

    void savePasswordRecoveryTokenForUser(User user, String code);
}
