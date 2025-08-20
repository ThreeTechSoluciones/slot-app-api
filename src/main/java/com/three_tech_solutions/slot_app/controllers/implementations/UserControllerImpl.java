package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.UserController;
import com.three_tech_solutions.slot_app.controllers.responses.PriceResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@AllArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;


    @Override
    public List<StudentResponse> getUserStudents(UUID userId, String studentName, String studentLastname, String studentDni) {
        return userService.getUserStudents(
                userId,
                studentName,
                studentLastname,
                studentDni
        );
    }

    @Override
    public List<PriceResponse> getUserPrices(UUID userId) {
        return userService.getUserPrices(userId);
    }
}
