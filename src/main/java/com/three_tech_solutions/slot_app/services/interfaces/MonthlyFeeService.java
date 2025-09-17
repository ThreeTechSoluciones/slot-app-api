package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.data.models.Student;

public interface MonthlyFeeService {

    void createStudentsMonthlyFee();
    void createInitialMonthlyFee(Student student, CreateStudentRequest createStudentRequest);

}
