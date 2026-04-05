package com.three_tech_solutions.slot_app.cron_jobs.services.implementations;

import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobStatus;
import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobType;
import com.three_tech_solutions.slot_app.cron_jobs.data.models.CronJobAuditory;
import com.three_tech_solutions.slot_app.cron_jobs.data.repositories.CronJobAuditoryRepository;
import com.three_tech_solutions.slot_app.cron_jobs.services.interfaces.CronJobAuditoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Component
@AllArgsConstructor
@Transactional(propagation = REQUIRES_NEW)
public class CronJobAuditoryServiceImpl implements CronJobAuditoryService {
    private final CronJobAuditoryRepository cronJobAuditoryRepository;

    @Override
    public CronJobAuditory createCronJobExecution(CronJobType cronJobType) {
        return saveCronJob(
                new CronJobAuditory(cronJobType)
        );
    }

    @Override
    public void setCronJobExecutionSuccess(CronJobAuditory cronJobAuditory) {
        cronJobAuditory.addStatus(CronJobStatus.COMPLETED);
        saveCronJob(cronJobAuditory);
    }

    @Override
    public void setCronJobExecutionFailure(CronJobAuditory cronJobAuditory, String errorMessage) {
        cronJobAuditory.addStatus(CronJobStatus.ERROR, errorMessage);
        saveCronJob(cronJobAuditory);
    }


    private CronJobAuditory saveCronJob(CronJobAuditory cronJobAuditory) {
        return this.cronJobAuditoryRepository.save(
                cronJobAuditory
        );
    }
}
