package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.ListUserSlotsResponse;
import com.three_tech_solutions.slot_app.data.models.User;

import java.time.DayOfWeek;

public interface SlotService {
    void createSlot(CreateSlotRequest request);

    ListUserSlotsResponse getSlotsByDayOfWeek(User user, DayOfWeek dayOfWeek);
}
