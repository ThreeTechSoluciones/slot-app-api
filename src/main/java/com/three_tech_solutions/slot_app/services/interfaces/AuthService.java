package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateUserRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RecoverPasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RestorePasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.ValidateTokenRequest;
import com.three_tech_solutions.slot_app.controllers.responses.RestorePasswordResponse;
import com.three_tech_solutions.slot_app.controllers.responses.SignInResponse;

public interface AuthService {
    SignInResponse signIn(String username);

    void createUser(CreateUserRequest createUserRequest);

    RestorePasswordResponse restorePassword(RestorePasswordRequest restorePasswordRequest);

    void confirmRecoverPassword(RecoverPasswordRequest recoverPasswordRequest);

    void validateToken(ValidateTokenRequest request);

    SignInResponse refreshToken(String refreshToken);
}
