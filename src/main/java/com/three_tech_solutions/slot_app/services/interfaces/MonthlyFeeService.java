package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentMonthlyFeeResponseDto;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.models.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MonthlyFeeService {

    void createStudentsMonthlyFee();
    void createInitialMonthlyFee(Student student, CreateStudentRequest createStudentRequest);
    void payMonthlyFee(UUID monthlyFeeId);
    List<StudentMonthlyFeeResponseDto> getMonthlyFeesByStudent(
            UUID studentId,
            String month,
            LocalDate expirationDate,
            MonthlyFeeStatus status
    );
}
