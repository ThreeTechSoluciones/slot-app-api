package com.three_tech_solutions.slot_app.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record NewCalendarResponse(
        List<Day> days,
        List<SlotTime> times,
        SpecificSlotResponse[][] slots
) {
    public record Day(
            DayOfWeek dayOfWeek,
            int numberOfDay
    ) {}

    public record SlotTime(
            @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,
            @JsonFormat(pattern = "HH:mm")
            LocalTime endTime
    ) {}
}
