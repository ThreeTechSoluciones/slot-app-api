package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentMonthlyFeeResponse;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MonthlyFeeService {

    void createStudentsMonthlyFee();
    void createInitialMonthlyFee(Student student, CreateStudentRequest createStudentRequest);
    void payMonthlyFee(UUID monthlyFeeId);
    Page<StudentMonthlyFeeResponse> getMonthlyFeesByStudent(
            Student student,
            String month,
            LocalDate expirationDate,
            MonthlyFeeStatus status,
            Pageable pageable
    );

    StudentMonthlyFeeResponse createMonthlyFeeForStudent(Student student);
    int findAssociatedMonthlyFeeNumber(Payment payment);
    MonthlyFee getMonthlyFeeById(UUID monthlyFeeId);
    void deleteMonthlyFee(MonthlyFee monthlyFee);
}
