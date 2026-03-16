package com.three_tech_solutions.slot_app.controllers.responses;

public record UserPreferencesResponse(
        byte capacity,
        long slotDurationMinutes
) {
}
