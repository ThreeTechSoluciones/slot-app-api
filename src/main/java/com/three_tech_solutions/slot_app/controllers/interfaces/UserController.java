package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@RequestMapping("/users")
public interface UserController {
    @GetMapping("/{userId}/students")
    List<StudentResponse> getUserStudents(
            @PathVariable UUID userId,
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required=false, defaultValue="") String orderBy,
            @RequestParam(required=false, defaultValue="") String orderDirection
    );

    @GetMapping("/{userId}/plans")
    List<PlanResponse> getUserPlans(@PathVariable UUID userId);
}
