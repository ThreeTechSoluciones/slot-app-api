package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/slots")
public interface SlotController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createSlot(@RequestBody CreateSlotRequest request);

    @PostMapping("/{slotId}/students/{studentId}")
    void addStudentToSlot(@PathVariable UUID slotId, @PathVariable UUID studentId);
}
