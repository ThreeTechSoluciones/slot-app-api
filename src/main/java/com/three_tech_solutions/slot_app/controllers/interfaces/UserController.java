package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.UpdateUserCapacityRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserPreferencesResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotsResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@RequestMapping("/users")
public interface UserController {
    @GetMapping("/{userId}/students")
    Page<StudentResponse> getUserStudents(
            @PathVariable UUID userId,
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "") Boolean isActive,
            @PageableDefault(size = 20) Pageable pageable
    );

    @GetMapping("/{userId}/plans")
    List<PlanResponse> getUserPlans(
            @PathVariable UUID userId,
            @RequestParam(required = false, defaultValue = "") String planName
    );

    @PatchMapping("/{userId}/capacity")
    void updateUserCapacityPreference(@PathVariable UUID userId, @Valid @RequestBody UpdateUserCapacityRequest updateUserCapacityRequest);

    @GetMapping("/{userId}/slots")
    UserSlotsResponse getSlotsByDayOfWeek(@PathVariable UUID userId, @RequestParam DayOfWeek dayOfWeek);

    @GetMapping("/{userId}/userPreferences")
    UserPreferencesResponse getUserPreferences (@PathVariable UUID userId);

}
