package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotsResponse;
import com.three_tech_solutions.slot_app.data.models.User;

import java.time.DayOfWeek;

import java.util.UUID;

public interface SlotService {
    void createSlot(CreateSlotRequest request);

    UserSlotsResponse getSlotsByDayOfWeek(User user, DayOfWeek dayOfWeek);

    void deleteSlot(UUID slotId);
}
