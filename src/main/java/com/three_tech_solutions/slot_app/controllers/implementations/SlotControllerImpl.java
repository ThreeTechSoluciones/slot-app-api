package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.SlotController;
import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.services.interfaces.SlotService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class SlotControllerImpl implements SlotController {
    private SlotService slotService;
    @Override
    public void createSlot(CreateSlotRequest request) {
        slotService.createSlot(request);
    }

    @Override
    public void deleteSlot(UUID slotId) {
        slotService.deleteSlot(slotId);
    }
}
