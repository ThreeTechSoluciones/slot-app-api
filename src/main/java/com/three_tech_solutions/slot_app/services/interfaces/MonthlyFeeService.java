package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.context.InitialPaymentContext;
import com.three_tech_solutions.slot_app.controllers.responses.StudentMonthlyFeeResponse;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MonthlyFeeService {

    void createStudentsMonthlyFee();
    void createInitialMonthlyFee(Student student, InitialPaymentContext initialPaymentContext);
    void payMonthlyFee(UUID monthlyFeeId);
    List<StudentMonthlyFeeResponse> getMonthlyFeesByStudent(
            Student student,
            String month,
            LocalDate expirationDate,
            MonthlyFeeStatus status
    );

    StudentMonthlyFeeResponse createMonthlyFeeForStudent(Student student);
    int findAssociatedMonthlyFeeNumber(Payment payment);
}
