package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotsResponse;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;

import java.time.DayOfWeek;
import java.util.UUID;

public interface SlotService {
    void createSlot(CreateSlotRequest request);

    void addStudentToSlot(UUID slotId, UUID studentId);

    UserSlotsResponse getSlotsByDayOfWeek(User user, DayOfWeek dayOfWeek);

    UserSlotResponse updateSlot(UUID slotId, UpdateSlotRequest updateSlotRequest);

    void addStudentToSlot(UUID slotId, Student student);

    void deleteSlot(UUID slotId);
}
