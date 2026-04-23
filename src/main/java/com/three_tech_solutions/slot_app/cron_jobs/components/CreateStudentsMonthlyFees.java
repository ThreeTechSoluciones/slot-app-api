package com.three_tech_solutions.slot_app.cron_jobs.components;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.components.monthly_fee_processors.factory.MonthlyFeeProcessorFactory;
import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobType;
import com.three_tech_solutions.slot_app.cron_jobs.data.models.CronJobAuditory;
import com.three_tech_solutions.slot_app.cron_jobs.services.interfaces.CronJobAuditoryService;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.services.interfaces.MonthlyFeeService;
import com.three_tech_solutions.slot_app.services.interfaces.NotificationService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class CreateStudentsMonthlyFees {

    private final StudentService studentService;
    private final MonthlyFeeProcessorFactory monthlyFeeProcessorFactory;
    private final MonthlyFeeService monthlyFeeService;
    private final CronJobAuditoryService cronJobAuditoryService;
    private final NotificationService notificationService;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void createStudentsMonthlyFee() {
        log.info("Iniciando proceso de creacion de pagos");
        CronJobAuditory cronJobAuditory = cronJobAuditoryService.createCronJobExecution(CronJobType.CREATE_STUDENTS_MONTHLY_FEES);
        try {
            List<Student> students = studentService.getStudents();
            students.forEach(student -> {
                MonthlyFeeProcessor monthlyFeeProcessor = monthlyFeeProcessorFactory.getPaymentProcessor(student.getPaymentPlan().getPaymentPlanName());
                Optional<MonthlyFee> monthlyFee = monthlyFeeProcessor.createStudentMonthlyFee(student, getMonthlyFeeNumber());
                monthlyFee
                        .ifPresentOrElse(
                                mf -> {
                                    monthlyFeeService.saveMonthlyFee(mf);
                                    notificationService.notifyNewMonthlyFee(student, mf);
                                    log.info("Pago creado para el estudiante {}", student);
                                },
                                () -> log.info("No se creó pago para el estudiante {}", student)
                        );
            });
            cronJobAuditoryService.setCronJobExecutionSuccess(cronJobAuditory);
        } catch (Exception e) {
            log.error("Hubo un error al crear los pagos de los estudiantes", e);
            cronJobAuditoryService.setCronJobExecutionFailure(cronJobAuditory, e.getMessage());
            throw e;
        }
    }

    private int getMonthlyFeeNumber() {
        return monthlyFeeService.getLastMonthlyFeeNumber();
    }


}
