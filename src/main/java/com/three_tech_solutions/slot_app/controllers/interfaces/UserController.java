package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@RequestMapping("/users")
public interface UserController {
    @GetMapping("/{userId}/students")
    Page<StudentResponse> getUserStudents(
            @PathVariable UUID userId,
            @RequestParam(required = false, defaultValue = "") String filter,
            @PageableDefault(size = 20, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable
    );

    @GetMapping("/{userId}/plans")
    List<PlanResponse> getUserPlans(@PathVariable UUID userId);
}
