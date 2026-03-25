package com.three_tech_solutions.slot_app.controllers.requests;

public record RecoverPasswordRequest(
        String sername,
        String password,
        String repeatedPassword,
        int token
) {
}
