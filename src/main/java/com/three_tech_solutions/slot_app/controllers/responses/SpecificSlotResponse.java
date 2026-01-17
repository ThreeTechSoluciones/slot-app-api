package com.three_tech_solutions.slot_app.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.three_tech_solutions.slot_app.data.enums.SpecificSlotDetailStatus;
import com.three_tech_solutions.slot_app.data.enums.SpecificSlotStatus;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;

import java.time.LocalDate;
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
        SpecificSlotResponseStatus status,
        List<Student> students
) {
    public record Student(
            UUID id,
            String fullName,
            SpecificSlotDetailStatus status
    ){}

    public enum SpecificSlotResponseStatus {
        FINALIZED,
        IN_PROGRESS,
        FUTURE,
        CANCELED;

        public static SpecificSlotResponseStatus calculateStatus(SpecificSlot specificSlot) {
            if (specificSlot.getStatus() == SpecificSlotStatus.CANCELED) {
                return CANCELED;
            }

            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            LocalDate slotDate = specificSlot.getSlotDate();
            if (slotDate.isBefore(today)) {
                return FINALIZED;
            }

            if (slotDate.isEqual(today)) {
                LocalTime start = specificSlot.getStartTime();
                LocalTime end = specificSlot.getEndTime();
                if (start.isBefore(now) && end.isAfter(now)) {
                    return IN_PROGRESS;
                }
            }

            return FUTURE;
        }
    }
}
