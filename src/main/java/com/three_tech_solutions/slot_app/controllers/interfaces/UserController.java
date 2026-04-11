package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.UpdateUserCapacityRequest;
import com.three_tech_solutions.slot_app.controllers.responses.*;
import com.three_tech_solutions.slot_app.data.enums.CalendarViewType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
            @RequestParam(required = false, defaultValue = "false") boolean filterByAbsences,
            @PageableDefault(size = 20) Pageable pageable
    );

    @GetMapping("/{userId}/plans")
    Page<PlanResponse> getUserPlans(
            @PathVariable UUID userId,
            @RequestParam(required = false, defaultValue = "") String planName,
            Pageable pageable
    );

    @PatchMapping("/{userId}/capacity")
    void updateUserCapacityPreference(@PathVariable UUID userId, @Valid @RequestBody UpdateUserCapacityRequest updateUserCapacityRequest);

    @GetMapping("/{userId}/slots")
    List<UserSlotsByDayResponse> getSlotsByDayOfWeek(@PathVariable UUID userId, @RequestParam(required = false) DayOfWeek dayOfWeek);

    @GetMapping("/{userId}/calendar")
    CalendarResponse getCalendarView(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "WEEKLY") CalendarViewType viewType,
            @RequestParam(required = false) LocalDate date
    );

    @GetMapping("/{userId}/userPreferences")
    UserPreferencesResponse getUserPreferences (@PathVariable UUID userId);
}
