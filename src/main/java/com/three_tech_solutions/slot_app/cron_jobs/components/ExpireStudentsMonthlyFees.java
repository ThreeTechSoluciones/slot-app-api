package com.three_tech_solutions.slot_app.cron_jobs.components;

import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobType;
import com.three_tech_solutions.slot_app.cron_jobs.data.models.CronJobAuditory;
import com.three_tech_solutions.slot_app.cron_jobs.services.interfaces.CronJobAuditoryService;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.services.interfaces.MonthlyFeeService;
import com.three_tech_solutions.slot_app.services.interfaces.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class ExpireStudentsMonthlyFees {

    private final CronJobAuditoryService cronJobAuditoryService;
    private final MonthlyFeeService monthlyFeeService;
    private final NotificationService notificationService;

    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    public void expireStudentsMonthlyFees() {
        log.info("Iniciando proceso de expiración de cuotas");
        CronJobAuditory cronJobAuditory = cronJobAuditoryService.createCronJobExecution(CronJobType.EXPIRE_STUDENTS_MONTHLY_FEES);
        try {
            List<MonthlyFee> expiredFees = monthlyFeeService.findExpiredMonthlyFees(LocalDate.now());

            expiredFees.forEach(monthlyFee -> {
                try {
                    monthlyFee.updateStatus(MonthlyFeeStatus.OUT_OF_TIME);
                    notificationService.notifyMonthlyFeeExpiration(monthlyFee);
                } catch (Exception e) {
                    log.error("Error expirando cuota {}", monthlyFee.getId(), e);
                }
            });

            monthlyFeeService.saveAllMonthlyFees(expiredFees);
            cronJobAuditoryService.setCronJobExecutionSuccess(cronJobAuditory);
            log.info("Finalizó proceso de expiración");
        } catch (Exception e) {
            log.error("Hubo un error al expirar las cuotas", e);
            cronJobAuditoryService.setCronJobExecutionFailure(cronJobAuditory, e.getMessage());
            throw e;
        }
    }
}
