package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.User;

import java.time.LocalDate;
import java.util.List;

public interface SpecificSlotService {
    List<SpecificSlot> getAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
