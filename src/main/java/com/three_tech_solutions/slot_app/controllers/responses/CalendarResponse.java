package com.three_tech_solutions.slot_app.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record CalendarResponse(
        DayOfWeek dayOfWeek,
        int numberOfDay,
        List<SpecificSlotResponse> slots
) {
    public record Time(
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime
    ){}
}
