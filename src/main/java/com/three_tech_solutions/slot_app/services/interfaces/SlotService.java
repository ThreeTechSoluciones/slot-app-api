package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotResponse;

import java.util.UUID;

public interface SlotService {
    void createSlot(CreateSlotRequest request);

    UserSlotResponse updateSlot(UUID slotId, UpdateSlotRequest updateSlotRequest);
}
