package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.UserController;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateUserCapacityRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserPreferencesResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotsResponse;
import com.three_tech_solutions.slot_app.data.enums.StudentSituation;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
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
            Pageable pageable
    ) {
        return userService.getUserStudents(
                userId,
                filter,
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
    public List<PlanResponse> getUserPlans(UUID userId, String planName) {
        return userService.getUserPlans(userId, planName);
    }

    @Override
    public void updateUserCapacityPreference(UUID userId, UpdateUserCapacityRequest updateUserCapacityRequest) {
        userService.updateUserCapacityPreference(userId, updateUserCapacityRequest);
    }

    @Override
    public UserSlotsResponse getSlotsByDayOfWeek(UUID userId, DayOfWeek dayOfWeek) {
        return userService.getSlotsByDayOfWeek(userId, dayOfWeek);
    }

    @Override
    public UserPreferencesResponse getUserPreferences(@PathVariable UUID userId){
        return userService.getUserPreferences(userId);
    };
}
