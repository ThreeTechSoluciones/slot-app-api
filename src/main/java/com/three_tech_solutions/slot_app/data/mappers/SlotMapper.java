package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.StudentSlotResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotResponse;
import com.three_tech_solutions.slot_app.data.models.Slot;
import org.springframework.stereotype.Component;

@Component
public class SlotMapper {
    public UserSlotResponse toSlotResponse(Slot slot, int usedCapacity) {
        return new UserSlotResponse(
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getCapacity(),
                usedCapacity
        );
    }

    public StudentSlotResponse toStudentSlotResponse(Slot slot) {
        return new StudentSlotResponse(
                slot.getId(),
                slot.getDayOfWeek(),
                slot.getStartTime(),
                slot.getEndTime()
        );
    }
}
