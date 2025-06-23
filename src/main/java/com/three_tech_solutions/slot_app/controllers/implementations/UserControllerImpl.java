package com.three_tech_solutions.slot_app.controllers.implementations;
import com.three_tech_solutions.slot_app.controllers.interfaces.UserController;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.services.implementations.UserServiceImpl;
import com.three_tech_solutions.slot_app.services.interfaces.PingService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;
    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<Student> user(UUID userId) {
        return userService.getUserStudents(userId);
    }
}
