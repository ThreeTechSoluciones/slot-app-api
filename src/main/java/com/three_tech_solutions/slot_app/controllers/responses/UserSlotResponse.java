package com.three_tech_solutions.slot_app.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record UserSlotResponse(
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,
        int maxCapacity,
        int usedCapacity
){}
