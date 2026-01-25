package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.responses.StudentPaymentsMetricResponse;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.services.interfaces.MetricService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MetricServiceImpl implements MetricService {

    private final StudentService studentService;

    @Override
    public StudentPaymentsMetricResponse getStudentPaymentsMetric(UUID studentId) {
        List<MonthlyFee> monthlyFees = getStudentMonthlyFees(studentId);
        int onTimeCount = 0;
        int outstandingCount = 0;
        int payedCount = 0;

        for (MonthlyFee monthlyFee : monthlyFees) {
            switch (monthlyFee.getCurrentStatus()) {
                case PENDING, OUT_OF_TIME -> outstandingCount++;
                case PAYED -> {
                    payedCount++;
                    onTimeCount++;
                }
                case PAYED_OUT_OF_TIME -> payedCount++;
            }
        }

        return new StudentPaymentsMetricResponse(
            onTimeCount,
            outstandingCount,
            payedCount
        );
    }

    private List<MonthlyFee> getStudentMonthlyFees(UUID studentId) {
        return studentService.getStudentByIdOrThrowExcepion(studentId).getMonthlyFees();
    }
}
