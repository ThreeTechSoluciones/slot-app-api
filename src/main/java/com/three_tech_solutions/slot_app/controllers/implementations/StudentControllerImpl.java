package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.StudentController;
import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentMonthlyFeeResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@RestController
@AllArgsConstructor
public class StudentControllerImpl implements StudentController {
    private final StudentService studentService;


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

    @Override
    public StudentResponse updateStudent(UUID studentId, UpdateStudentRequest studentUpdated){
        return studentService.updateStudent(studentId, studentUpdated);
    }

    @Override
    public List<StudentMonthlyFeeResponse> getStudentMonthlyFees(UUID studentId, String month, LocalDate expirationDate, MonthlyFeeStatus status) {
        return studentService.getStudentMonthlyFees(studentId, month, expirationDate, status);
    }

    @Override
    public StudentMonthlyFeeResponse createStudentMonthlyFee(UUID studentId) {
        return studentService.createStudentMonthlyFee(studentId);
    }
}