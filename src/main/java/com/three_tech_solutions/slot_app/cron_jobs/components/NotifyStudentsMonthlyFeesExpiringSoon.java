package com.three_tech_solutions.slot_app.cron_jobs.components;

import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobType;
import com.three_tech_solutions.slot_app.cron_jobs.data.models.CronJobAuditory;
import com.three_tech_solutions.slot_app.cron_jobs.services.interfaces.CronJobAuditoryService;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.services.interfaces.NotificationService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NotifyStudentsMonthlyFeesExpiringSoon {

    private final StudentService studentService;
    private final NotificationService notificationService;
    private final CronJobAuditoryService cronJobAuditoryService;

    @Transactional
    @Scheduled(cron = "0 0 2 8 * *")
    public void notifyStudentsAboutExpiringFees() {
        log.info("Iniciando proceso de notificación de cuotas por vencer");
        CronJobAuditory cronJobAuditory = cronJobAuditoryService.createCronJobExecution(CronJobType.NOTIFY_STUDENTS_MONTHLY_FEES_EXPIRING_SOON);

        try {
            getStudentsWithBeginningOfMonthPlan().stream()
                    .filter(this::studentHasPendingMonthlyFee)
                    .forEach(this::notifyStudentAboutExpiringFee);

            cronJobAuditoryService.setCronJobExecutionSuccess(cronJobAuditory);
            log.info("Finalizó proceso de notificación de cuotas por vencer");
        } catch (Exception e) {
            log.error("Hubo un error al notificar cuotas por vencer", e);
            cronJobAuditoryService.setCronJobExecutionFailure(cronJobAuditory, e.getMessage());
            throw e;
        }
    }

    private List<Student> getStudentsWithBeginningOfMonthPlan() {
        return studentService.findByPaymentPlanName(PaymentPlanName.BEGINNING_OF_MONTH);
    }

    private boolean studentHasPendingMonthlyFee(Student student) {
        return student.getLatestMonthlyFee()
                .map(this::monthlyFeeIsPending)
                .orElse(false);
    }

    private boolean monthlyFeeIsPending(MonthlyFee monthlyFee) {
        return monthlyFee.getCurrentStatus() == MonthlyFeeStatus.PENDING;
    }

    private void notifyStudentAboutExpiringFee(Student student) {
        student.getLatestMonthlyFee()
                .ifPresent(monthlyFee -> {
                    try {
                        notificationService.notifyMonthlyFeeExpiringSoon(monthlyFee);
                    } catch (Exception e) {
                        log.error("Error notificando cuota por vencer del estudiante {}", student, e);
                    }
                });
    }
}