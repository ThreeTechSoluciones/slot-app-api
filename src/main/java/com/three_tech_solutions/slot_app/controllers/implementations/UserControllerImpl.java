package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.UserController;
import com.three_tech_solutions.slot_app.controllers.requests.RecoverPasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateUserCapacityRequest;
import com.three_tech_solutions.slot_app.controllers.responses.*;
import com.three_tech_solutions.slot_app.data.enums.CalendarViewType;
import com.three_tech_solutions.slot_app.data.enums.StudentSituation;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@RestController
@AllArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public Page<StudentResponse> getUserStudents(
            UUID userId,
            String filter,
            String status,
            Boolean isActive,
            boolean filterByAbsences,
            Pageable pageable
    ) {
        return userService.getUserStudents(
                userId,
                filter,
                filterByAbsences,
                getStudentSituationFilter(status),
                isActive,
                pageable
        );
    }

    private static StudentSituation getStudentSituationFilter(String status) {
        return Optional
                .ofNullable(status)
                .map(s -> {
                    try {
                        return StudentSituation.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        throw new ResponseStatusException(BAD_REQUEST, "Invalid student situation: " + s + ". Valid values are: " + List.of(StudentSituation.values()));
                    }
                })
                .orElse(null);
    }

    @Override
    public Page<PlanResponse> getUserPlans(UUID userId, String planName, Pageable pageable) {
        return userService.getUserPlans(userId, planName, pageable);
    }

    @Override
    public void updateUserCapacityPreference(UUID userId, UpdateUserCapacityRequest updateUserCapacityRequest) {
        userService.updateUserCapacityPreference(userId, updateUserCapacityRequest);
    }

    @Override
    public List<UserSlotsByDayResponse> getSlotsByDayOfWeek(UUID userId, DayOfWeek dayOfWeek) {
        return userService.getSlotsByDayOfWeek(userId, dayOfWeek);
    }

    @Override
    public CalendarResponse getCalendarView(UUID userId, CalendarViewType viewType, LocalDate date) {
        return userService.getCalendar(
                userId,
                viewType,
                Optional.ofNullable(date).orElse(LocalDate.now())
        );
    }

    @Override
    public UserPreferencesResponse getUserPreferences(@PathVariable UUID userId){
        return userService.getUserPreferences(userId);
    };

    @Override
    public void recoverPassword(@Valid @RequestBody RecoverPasswordRequest request) {
        userService.recoverPassword(request);
    }
}
