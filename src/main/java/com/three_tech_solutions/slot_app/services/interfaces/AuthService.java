package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.SignInResponse;

public interface AuthService {
    SignInResponse signIn(String username);
}
