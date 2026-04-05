package com.three_tech_solutions.slot_app.controllers.requests;

import jakarta.validation.constraints.NotBlank;

public record RestorePasswordRequest (
    @NotBlank
    String username
) { }
