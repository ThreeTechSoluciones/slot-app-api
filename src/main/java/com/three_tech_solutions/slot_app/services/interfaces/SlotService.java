package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotResponse;

import java.util.UUID;

import com.three_tech_solutions.slot_app.controllers.responses.UserSlotsByDayResponse;
import com.three_tech_solutions.slot_app.data.models.User;

import java.time.DayOfWeek;
import java.util.List;

public interface SlotService {
    void createSlot(CreateSlotRequest request);

    void addStudentToSlot(UUID slotId, UUID studentId);

    List<UserSlotsByDayResponse> getSlotsByDayOfWeek(User user, DayOfWeek dayOfWeek);

    UserSlotResponse updateSlot(UUID slotId, UpdateSlotRequest updateSlotRequest);

    void deleteSlot(UUID slotId);
}
