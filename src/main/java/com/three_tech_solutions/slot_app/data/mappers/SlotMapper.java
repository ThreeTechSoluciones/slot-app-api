package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.SlotResponse;
import com.three_tech_solutions.slot_app.data.models.Slot;
import org.springframework.stereotype.Component;

@Component
public class SlotMapper {
    public SlotResponse toSlotResponse(Slot slot, int usedCapacity) {
        return new SlotResponse(
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getCapacity(),
                usedCapacity
        );
    }
}
