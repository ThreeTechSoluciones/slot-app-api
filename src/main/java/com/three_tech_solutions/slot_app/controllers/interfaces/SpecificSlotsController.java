package com.three_tech_solutions.slot_app.controllers.interfaces;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RequestMapping("/specific-slots")
public interface SpecificSlotsController {
    @PostMapping("/{specificSlotId}/cancel")
    void cancelSpecificSlot(
            @PathVariable UUID specificSlotId,
            @RequestParam(required = false, defaultValue = "true") boolean studentsMustRecoverSlot
    );
}
