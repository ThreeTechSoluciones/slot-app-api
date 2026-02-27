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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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
    public StudentDetailsResponse getStudentDetails(UUID studentId) {
        return studentService.getStudentDetails(studentId);
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
    public Page<StudentMonthlyFeeResponse> getStudentMonthlyFees(UUID studentId, String month, LocalDate expirationDate, MonthlyFeeStatus status, Pageable pageable) {
        return studentService.getStudentMonthlyFees(studentId, month, expirationDate, status, pageable);
    }

    @Override
    public StudentMonthlyFeeResponse createStudentMonthlyFee(UUID studentId) {
        return studentService.createStudentMonthlyFee(studentId);
    }

    @Override
    public void validateIfDniExists(String dni) {
        studentService.validateIfDniExists(dni);
    }

    @Override
    public void registerStudentAbsenceForSpecificSlot(UUID studentId, UUID specificSlotId) {
        studentService.registerStudentAbsenceForSpecificSlot(studentId, specificSlotId);
    }

    @Override
    public void recoverSlot(UUID studentId, UUID specificSlotId) {
        studentService.recoverSlot(studentId, specificSlotId);
    }
}