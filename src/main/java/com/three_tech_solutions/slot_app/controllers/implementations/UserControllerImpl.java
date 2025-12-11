package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.UserController;
import com.three_tech_solutions.slot_app.controllers.responses.ListUserSlotsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;


@RestController
@AllArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;


    @Override
    public List<StudentResponse> getUserStudents(UUID userId, String filter) {
        return userService.getUserStudents(
                userId,
                filter
        );
    }

    @Override
    public List<PlanResponse> getUserPlans(UUID userId) {
        return userService.getUserPlans(userId);
    }

    @Override
    public ListUserSlotsResponse getSlotsByDayOfWeek(UUID userId, DayOfWeek dayOfWeek) {
        return userService.getSlotsByDayOfWeek(userId, dayOfWeek);
    }
}
