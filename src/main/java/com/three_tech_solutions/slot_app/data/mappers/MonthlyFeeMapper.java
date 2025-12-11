package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.StudentMonthlyFeeResponse;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;

public class MonthlyFeeMapper {
    public static StudentMonthlyFeeResponse toStudentMonthlyFeeResponse(MonthlyFee monthlyFee) {
        return new StudentMonthlyFeeResponse(
                monthlyFee.getId(),
                monthlyFee.getNumber(),
                monthlyFee.getExpirationDate().getMonth().name(),
                monthlyFee.getExpirationDate(),
                monthlyFee.getAmount(),
                monthlyFee.getCurrentStatus(),
                monthlyFee.getPayment() != null ? monthlyFee.getPayment().getId() : null
        );
    }
}
