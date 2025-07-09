package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.responses.SignInResponse;
import com.three_tech_solutions.slot_app.services.interfaces.AuthService;
import com.three_tech_solutions.slot_app.services.interfaces.JsonWebTokenService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final JsonWebTokenService jsonWebTokenService;

    @Override
    public SignInResponse signIn(String username) {
        return new SignInResponse(
                userService.loadUserByUsername(username).getId(),
                jsonWebTokenService.getAccessToken(username),
                jsonWebTokenService.getRefreshToken(username)
        );
    }
}
