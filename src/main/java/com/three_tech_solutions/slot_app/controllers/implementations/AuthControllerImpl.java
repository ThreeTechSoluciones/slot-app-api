package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.AuthController;
import com.three_tech_solutions.slot_app.controllers.requests.CreateUserRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RecoverPasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RestorePasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.ValidateTokenRequest;
import com.three_tech_solutions.slot_app.controllers.responses.RestorePasswordResponse;
import com.three_tech_solutions.slot_app.controllers.responses.SignInResponse;
import com.three_tech_solutions.slot_app.services.interfaces.AuthService;
import com.three_tech_solutions.slot_app.utils.AuthUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.three_tech_solutions.slot_app.utils.AuthUtils.removeBearerPrefix;

@RestController
@AllArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;

    @Override
    public SignInResponse signIn(String authorization) {
        return authService.signIn(
                AuthUtils.extractUsername(authorization)
        );
    }

    @Override
    public void signUp(CreateUserRequest createUserRequest) {
        authService.createUser(createUserRequest);
    }

    @Override
    public RestorePasswordResponse restorePassword(RestorePasswordRequest restorePasswordRequest) {
        return authService.restorePassword(restorePasswordRequest);
    }

    @Override
    public void recoverPassword(RecoverPasswordRequest recoverPasswordRequest) {
        authService.confirmRecoverPassword(recoverPasswordRequest);
    }

    @Override
    public void validateTokenAndDisableIt(ValidateTokenRequest request) {
        authService.validateToken(request);
    }

    @Override
    public SignInResponse refreshToken(String refreshToken) {
        return authService.refreshToken(removeBearerPrefix(refreshToken));
    }



}
