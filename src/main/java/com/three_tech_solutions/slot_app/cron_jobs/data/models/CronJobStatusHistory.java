package com.three_tech_solutions.slot_app.cron_jobs.data.models;

import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CronJobStatusHistory {
    String description;
    @Enumerated(EnumType.STRING)
    CronJobStatus status;
    LocalDateTime createdAt = LocalDateTime.now();
    @Id
    UUID id = UUID.randomUUID();

    public CronJobStatusHistory(CronJobStatus status) {
        this.status = status;
    }

    public CronJobStatusHistory(CronJobStatus status, String description) {
        this.status = status;
        this.description = description;
    }
}
