package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.dto.CreateStudentRequest;
import com.three_tech_solutions.slot_app.dto.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.dto.StudentResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/students")
public interface StudentController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    StudentResponse createStudent(@Valid @RequestBody CreateStudentRequest studentDTO);

    @GetMapping("/{studentId}")
    StudentDetailsResponse getStudentById(@PathVariable UUID studentId);
}
