package com.three_tech_solutions.slot_app.controllers.requests;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public record CreateSlotRequest(
        DayOfWeek dayOfWeek,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        UUID userId
) {
}
