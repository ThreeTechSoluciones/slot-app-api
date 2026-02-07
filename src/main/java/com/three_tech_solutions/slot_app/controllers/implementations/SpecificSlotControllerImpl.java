package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.SpecificSlotsController;
import com.three_tech_solutions.slot_app.controllers.responses.SpecificSlotResponse;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class SpecificSlotControllerImpl implements SpecificSlotsController {

    private final SpecificSlotService specificSlotService;

    @Override
    public void cancelSpecificSlot(
            UUID specificSlotId,
            boolean studentsMustRecoverSlot
    ) {
        specificSlotService.cancelSpecificSlot(specificSlotId, studentsMustRecoverSlot);
    }

    @Override
    public List<SpecificSlotResponse.Student> getStudentsInSpecificSlot(UUID specificSlotId, String filter) {
        return specificSlotService.getStudentsInSpecificSlot(specificSlotId, filter);
    }
}
