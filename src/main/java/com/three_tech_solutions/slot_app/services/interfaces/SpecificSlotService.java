package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SpecificSlotService {
    List<SpecificSlot> getAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    SpecificSlot getSpecificSlotByIdOrThrowException(UUID specificSlotId);

    void saveSpecificSlot(SpecificSlot specificSlot);
}
