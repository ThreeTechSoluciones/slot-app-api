package com.three_tech_solutions.slot_app.controllers.responses;

import java.time.DayOfWeek;
import java.util.List;

public record CalendarResponse(
        DayOfWeek dayOfWeek,
        int numberOfDay,
        List<SpecificSlotResponse> slots
) {
}
