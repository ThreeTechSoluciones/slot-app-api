package com.three_tech_solutions.slot_app.cron_jobs.data.models;

import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobStatus;
import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class CronJobAuditory {

    @Enumerated(EnumType.STRING)
    CronJobType cronJobType;
    @OneToMany(cascade = CascadeType.ALL)
    List<CronJobStatusHistory> statusHistory;
    LocalDateTime cronJobUpdatedAt = LocalDateTime.now();
    LocalDateTime cronJobStartedAt = LocalDateTime.now();
    @Id
    UUID id = UUID.randomUUID();

    public CronJobAuditory(CronJobType cronJobType) {
        this.cronJobType = cronJobType;
        this.statusHistory = new ArrayList<>(
                List.of(new CronJobStatusHistory(CronJobStatus.STARTED) )
        );
    }

    public void addStatus(CronJobStatus cronJobStatus) {
        this.cronJobUpdatedAt = LocalDateTime.now();
        this.statusHistory.add(
                new CronJobStatusHistory(cronJobStatus)
        );
    }

    public void addStatus(CronJobStatus cronJobStatus, String description) {
        this.cronJobUpdatedAt = LocalDateTime.now();
        this.statusHistory.add(
                new CronJobStatusHistory(cronJobStatus, description)
        );
    }
}
