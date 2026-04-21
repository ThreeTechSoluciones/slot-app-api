package com.three_tech_solutions.slot_app.cron_jobs.services.interfaces;

import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobType;
import com.three_tech_solutions.slot_app.cron_jobs.data.models.CronJobAuditory;

public interface CronJobAuditoryService {
    CronJobAuditory createCronJobExecution(CronJobType cronJobType);

    void setCronJobExecutionSuccess(CronJobAuditory cronJobAuditory);

    void setCronJobExecutionFailure(CronJobAuditory cronJobAuditory, String message);
}
