package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateUserRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RecoverPasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RestorePasswordRequest;
import com.three_tech_solutions.slot_app.controllers.responses.SignInResponse;

public interface AuthService {
    SignInResponse signIn(String username);

    void createUser(CreateUserRequest createUserRequest);

    void restorePassword(RestorePasswordRequest restorePasswordRequest);

    void confirmRecoverPassword(RecoverPasswordRequest recoverPasswordRequest);
}
