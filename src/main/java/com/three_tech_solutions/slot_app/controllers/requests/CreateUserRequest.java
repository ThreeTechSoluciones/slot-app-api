package com.three_tech_solutions.slot_app.controllers.requests;

public record CreateUserRequest(
        String username,
        String password,
        String repeatedPassword,
        String businessName
) {
}
