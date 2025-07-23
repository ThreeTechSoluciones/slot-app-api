package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.dto.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.dto.StudentResponse;
import com.three_tech_solutions.slot_app.controllers.interfaces.StudentController;
import com.three_tech_solutions.slot_app.dto.CreateStudentRequest;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;


@RestController
public class StudentControllerImpl implements StudentController {
    private final StudentService studentService;

    public StudentControllerImpl(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public StudentResponse createStudent(CreateStudentRequest createStudentRequest) {
        return studentService.createStudent(createStudentRequest);
    }

    @Override
    public void deleteStudent(UUID studentId){
        studentService.deleteStudent(studentId);

    }

    @Override
    public StudentDetailsResponse getStudentById(UUID studentId) {
        return studentService.getStudentById(studentId);
    }

    @Override
    public void activateStudent(UUID studentId) {
        studentService.activateStudent(studentId);
    }
}