package com.three_tech_solutions.slot_app.cron_jobs.data.repositories;

import com.three_tech_solutions.slot_app.cron_jobs.data.models.CronJobAuditory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CronJobAuditoryRepository extends JpaRepository<CronJobAuditory, UUID> {
}
