package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotResponse;
import jakarta.validation.Valid;
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

    @PatchMapping("/{slotId}")
    UserSlotResponse updateSlot(@PathVariable UUID slotId, @RequestBody @Valid UpdateSlotRequest updateSlotRequest);


    @DeleteMapping("/{slotId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSlot(@PathVariable UUID slotId);
}
