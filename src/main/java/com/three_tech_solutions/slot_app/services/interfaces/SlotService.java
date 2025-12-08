package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;

import java.util.UUID;

public interface SlotService {
    void createSlot(CreateSlotRequest request);

    void addStudentToSlot(UUID slotId, UUID studentId);
}
