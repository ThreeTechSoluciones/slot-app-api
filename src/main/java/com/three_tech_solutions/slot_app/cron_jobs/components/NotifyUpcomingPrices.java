package com.three_tech_solutions.slot_app.cron_jobs.components;

import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobType;
import com.three_tech_solutions.slot_app.cron_jobs.data.models.CronJobAuditory;
import com.three_tech_solutions.slot_app.cron_jobs.services.interfaces.CronJobAuditoryService;
import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.Price;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.repositories.PlanRepository;
import com.three_tech_solutions.slot_app.data.repositories.StudentRepository;
import com.three_tech_solutions.slot_app.services.interfaces.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NotifyUpcomingPrices {

    private static final int DAYS_BEFORE_NOTIFICATION = 2;

    private final CronJobAuditoryService cronJobAuditoryService;
    private final PlanRepository planRepository;
    private final StudentRepository studentRepository;
    private final NotificationService notificationService;

    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    public void notifyUpcomingPrices() {
        log.info("Iniciando notificación de precios próximos");
        CronJobAuditory cronJobAuditory = cronJobAuditoryService.createCronJobExecution(CronJobType.NOTIFY_UPCOMING_PRICES);

        try {
            List<Plan> plans = planRepository.findAll();

            plans.forEach(plan -> {
                plan.getPrices().forEach(price -> {
                    if (shouldSendNotification(price)) {
                        notifyStudents(plan, price);
                    }
                });
            });

            cronJobAuditoryService.setCronJobExecutionSuccess(cronJobAuditory);
            log.info("Finalizó notificación de precios próximos");

        } catch (Exception e) {
            log.error("Error en notificación de precios", e);
            cronJobAuditoryService.setCronJobExecutionFailure(cronJobAuditory, e.getMessage());
            throw e;
        }
    }

    private void notifyStudents(Plan plan, Price price) {
        List<Student> students = studentRepository.findByPaymentPlan_Plan_Id(plan.getId());

        students.forEach(student -> {
            try {
                notificationService.notifyNewPrice(student, price);
            } catch (Exception e) {
                log.error("Error notificando estudiante {}", student.getId(), e);
            }
        });
    }

    private boolean shouldSendNotification(Price price) {
        return price.getDaysUntilActive() == DAYS_BEFORE_NOTIFICATION;
    }
}