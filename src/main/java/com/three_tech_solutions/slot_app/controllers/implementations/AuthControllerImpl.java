package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.AuthController;
import com.three_tech_solutions.slot_app.controllers.responses.SignInResponse;
import com.three_tech_solutions.slot_app.services.interfaces.AuthService;
import com.three_tech_solutions.slot_app.utils.BasicAuthUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public SignInResponse signIn(String authorization) {
        return authService.signIn(
                BasicAuthUtils.extractUsername(authorization)
        );
    }
}
