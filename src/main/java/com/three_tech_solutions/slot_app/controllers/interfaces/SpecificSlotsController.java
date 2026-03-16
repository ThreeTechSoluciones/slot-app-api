package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.SpecificSlotResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/specific-slots")
public interface SpecificSlotsController {
    @PostMapping("/{specificSlotId}/cancel")
    void cancelSpecificSlot(
            @PathVariable UUID specificSlotId,
            @RequestParam(required = false, defaultValue = "true") boolean studentsMustRecoverSlot
    );

    @GetMapping("/{specificSlotId}/students")
    List<SpecificSlotResponse.Student> getStudentsInSpecificSlot(
            @PathVariable UUID specificSlotId,
            @RequestParam(required = false, defaultValue = "") String filter
    );
}
