package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.UserController;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotsResponse;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateUserCapacityRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;


@RestController
@AllArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public Page<StudentResponse> getUserStudents(UUID userId, String filter, Pageable pageable) {
        return userService.getUserStudents(
                userId,
                filter,
                pageable
        );
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
}
