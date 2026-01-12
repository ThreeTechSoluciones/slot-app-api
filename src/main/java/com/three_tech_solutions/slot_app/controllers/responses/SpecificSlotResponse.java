package com.three_tech_solutions.slot_app.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.three_tech_solutions.slot_app.data.enums.SpecificSlotDetailStatus;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record SpecificSlotResponse(
        UUID id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,
        byte maxCapacity,
        int capacity,
        List<Student> students
) {
    public record Student(
            UUID id,
            String fullName,
            SpecificSlotDetailStatus status
    ){}
}
