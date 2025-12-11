package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.UserController;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@AllArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public Page<StudentResponse> getUserStudents(UUID userId, String filter, Integer page, Integer size) {
        return userService.getUserStudents(
                userId,
                filter,
                PageRequest.of(page, size)
        );
    }

    @Override
    public List<PlanResponse> getUserPlans(UUID userId, String planName) {
        return userService.getUserPlans(userId, planName);
    }
}
