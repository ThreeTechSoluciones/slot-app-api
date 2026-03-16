package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.responses.StudentPaymentsMetricResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentsSummaryMetricsResponse;
import com.three_tech_solutions.slot_app.data.enums.StudentSituation;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
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

        int paidCount = 0;
        int expiredCount = 0;
        int paidOutOfTimeCount = 0;

        for (MonthlyFee monthlyFee : monthlyFees) {

            switch (monthlyFee.getCurrentStatus()) {

                case PAYED ->
                        paidCount++;

                case PAYED_OUT_OF_TIME -> {
                    paidCount++;
                    paidOutOfTimeCount++;
                }

                case OUT_OF_TIME ->
                        expiredCount++;
            }
        }

        return new StudentPaymentsMetricResponse(
                paidCount,
                expiredCount,
                paidOutOfTimeCount
        );
    }

    @Override
    public StudentsSummaryMetricsResponse getStudentsSummaryMetrics() {
        List<Student> students = getAllStudents();

        int activeStudentsCount = 0;
        int activeStudentsWithDebtCount = 0;
        int inactiveStudentsWithDebtCount = 0;

        for (Student student : students) {

            if (student.isEnabled()) {
                activeStudentsCount++;

                if (student.getStudentSituation() == StudentSituation.CON_DEUDA) {
                    activeStudentsWithDebtCount++;
                }

            } else if (student.getStudentSituation() == StudentSituation.CON_DEUDA) {
                inactiveStudentsWithDebtCount++;
            }
        }

        return new StudentsSummaryMetricsResponse(
                activeStudentsCount,
                activeStudentsWithDebtCount,
                inactiveStudentsWithDebtCount
        );
    }

    private List<MonthlyFee> getStudentMonthlyFees(UUID studentId) {
        return studentService.getStudentByIdOrThrowExcepion(studentId).getMonthlyFees();
    }

    private List<Student> getAllStudents() {
        return studentService.getStudents();
    }
}
