package com.three_tech_solutions.slot_app.services.interfaces;


import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;

import java.util.UUID;

public interface StudentService {

    StudentResponse createStudent(CreateStudentRequest studentDTO);

    void deleteStudent(UUID studentId);

    StudentDetailsResponse getStudentById(UUID studentId);

    void activateStudent(UUID studentId);

    StudentResponse updateStudent(UUID studentId, UpdateStudentRequest studentUpdated);
}
