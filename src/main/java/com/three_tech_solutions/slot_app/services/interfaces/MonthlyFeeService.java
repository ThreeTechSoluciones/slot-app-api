package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.MonthlyFeePaymentResponse;
import com.three_tech_solutions.slot_app.data.models.Student;

import java.util.UUID;

public interface MonthlyFeeService {

    void createStudentsMonthlyFee();
    void createInitialMonthlyFee(Student student, CreateStudentRequest createStudentRequest);
    void payMonthlyFee(UUID monthlyFeeId);

}
