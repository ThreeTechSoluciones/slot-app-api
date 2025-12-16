package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.requests.UpdateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotResponse;
import com.three_tech_solutions.slot_app.data.models.Slot;
import org.springframework.stereotype.Service;

@Service
public class SlotMapper {
    public UserSlotResponse toSlotResponse(Slot slot, int usedCapacity) {
        return new UserSlotResponse(
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getCapacity(),
                usedCapacity
        );
    }

    public void updateSlot(Slot slot, UpdateSlotRequest request) {
        slot.setStartTime(request.startTime());
    }
}
