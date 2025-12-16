package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotsResponse;
import com.three_tech_solutions.slot_app.data.models.Slot;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;

import java.time.DayOfWeek;

import java.util.List;
import java.util.UUID;

public interface SlotService {
    void createSlot(CreateSlotRequest request);

    void addStudentToSlot(UUID slotId, UUID studentId);

    UserSlotsResponse getSlotsByDayOfWeek(User user, DayOfWeek dayOfWeek);

    List<Slot> getSlotsByStudent(Student student);
}
