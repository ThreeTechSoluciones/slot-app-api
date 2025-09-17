package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;

public interface PaymentService {

    void createStudentsPayment();
    MonthlyFee createInitialPayment(Student student, Byte extraClasses);

}
