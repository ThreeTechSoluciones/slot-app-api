package com.three_tech_solutions.slot_app.controllers.responses;

import java.util.List;

public record ListUserSlotsResponse(
        int numberOfSlots,
        List<UserSlotResponse> slots
) {}
