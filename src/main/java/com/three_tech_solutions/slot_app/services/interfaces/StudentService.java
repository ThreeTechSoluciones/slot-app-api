package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.dto.CreateStudentRequest;
import com.three_tech_solutions.slot_app.dto.StudentResponse;

public interface StudentService {

    StudentResponse createStudent(CreateStudentRequest studentDTO);

}
