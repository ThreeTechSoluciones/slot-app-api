package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.context.InitialPaymentContext;
import com.three_tech_solutions.slot_app.controllers.responses.StudentMonthlyFeeResponse;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface MonthlyFeeService {

    void createInitialMonthlyFee(Student student, InitialPaymentContext initialPaymentContext);
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

    int getLastMonthlyFeeNumber();

    MonthlyFee saveMonthlyFee(MonthlyFee monthlyFee);
}
