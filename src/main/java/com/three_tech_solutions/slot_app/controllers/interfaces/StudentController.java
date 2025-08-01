package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.dto.CreateStudentRequest;
import com.three_tech_solutions.slot_app.dto.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.dto.StudentResponse;
import com.three_tech_solutions.slot_app.dto.UpdateStudentRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RequestMapping("/students")
public interface StudentController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    StudentResponse createStudent(@Valid @RequestBody CreateStudentRequest studentDTO);


    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteStudent(@PathVariable UUID studentId);

    @GetMapping("/{studentId}")
    StudentDetailsResponse getStudentById(@PathVariable UUID studentId);

    @PatchMapping("/{studentId}")
    StudentResponse updateStudent (@PathVariable UUID studentId, UpdateStudentRequest studentUpdated);

    @PostMapping("/{studentId}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void activateStudent(@PathVariable UUID studentId);

}