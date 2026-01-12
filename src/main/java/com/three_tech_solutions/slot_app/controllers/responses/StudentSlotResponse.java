package com.three_tech_solutions.slot_app.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public record StudentSlotResponse(
    UUID slotId,
    DayOfWeek dayOfWeek,
    @JsonFormat(pattern = "HH:mm")
    LocalTime startTime,
    @JsonFormat(pattern = "HH:mm")
    LocalTime endTime
){}
