package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.ListSlotsResponse;

import java.time.DayOfWeek;
import java.util.UUID;

public interface SlotService {
    void createSlot(CreateSlotRequest request);

    ListSlotsResponse getSlotsByDayOfWeek(UUID userId, DayOfWeek dayOfWeek);
}
