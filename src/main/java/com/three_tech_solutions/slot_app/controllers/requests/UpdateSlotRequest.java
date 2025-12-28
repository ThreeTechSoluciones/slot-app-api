package com.three_tech_solutions.slot_app.controllers.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record UpdateSlotRequest(
        @NotNull (message = "La hora de inicio es obligatoria")
        LocalTime startTime
) {}
