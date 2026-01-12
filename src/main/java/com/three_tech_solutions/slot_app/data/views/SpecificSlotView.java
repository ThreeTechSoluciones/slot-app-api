package com.three_tech_solutions.slot_app.data.views;

import com.three_tech_solutions.slot_app.data.models.SpecificSlotDetail;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record SpecificSlotView(
        UUID id,
        LocalDate slotDate,
        LocalTime startTime,
        LocalTime endTime,
        byte capacity,
        DayOfWeek dayOfWeek,
        List<SpecificSlotDetail> specificSlotDetails
) {
}