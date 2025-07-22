package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.dto.CreateStudentRequest;
import com.three_tech_solutions.slot_app.dto.StudentResponse;

import java.util.UUID;

public interface StudentService {

    StudentResponse createStudent(CreateStudentRequest studentDTO);
    void deleteStudent(UUID studentId);
}
