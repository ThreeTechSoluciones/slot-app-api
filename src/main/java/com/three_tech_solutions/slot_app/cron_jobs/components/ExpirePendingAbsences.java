package com.three_tech_solutions.slot_app.cron_jobs.components;

import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobType;
import com.three_tech_solutions.slot_app.cron_jobs.data.models.CronJobAuditory;
import com.three_tech_solutions.slot_app.cron_jobs.services.interfaces.CronJobAuditoryService;
import com.three_tech_solutions.slot_app.data.enums.AbsenceStatus;
import com.three_tech_solutions.slot_app.data.models.Absence;
import com.three_tech_solutions.slot_app.data.repositories.AbsenceRepository;
import com.three_tech_solutions.slot_app.services.interfaces.AbsenceService;
import com.three_tech_solutions.slot_app.services.interfaces.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ExpirePendingAbsences implements AbsenceService {

    private final CronJobAuditoryService cronJobAuditoryService;
    private final AbsenceRepository absenceRepository;
    private final NotificationService notificationService;

    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    @Override
    public void expirePendingAbsences() {
        log.info("Iniciando proceso de expiración de ausencias");
        CronJobAuditory cronJobAuditory = cronJobAuditoryService.createCronJobExecution(CronJobType.EXPIRE_PENDING_ABSENCES);
        try {
            LocalDate today = LocalDate.now();
            List<Absence> pendingAbsences = getPendingAbsences();

            pendingAbsences.stream()
                    .filter(absence -> absence.isAboutToExpire(today))
                    .forEach(this::notifyRecoveryAboutToExpire);

            List<Absence> absencesToExpire = pendingAbsences.stream()
                    .filter(absence -> absence.isExpired(today))
                    .peek(absence -> absence.setStatus(AbsenceStatus.OUT_OF_TIME))
                    .toList();

            absenceRepository.saveAll(absencesToExpire);
            cronJobAuditoryService.setCronJobExecutionSuccess(cronJobAuditory);
        } catch (Exception e) {
            log.error("Hubo un error al expirar las ausencias pendientes", e);
            cronJobAuditoryService.setCronJobExecutionFailure(cronJobAuditory, e.getMessage());
            throw e;
        }
    }

    private void notifyRecoveryAboutToExpire(Absence absence) {
        try {
            notificationService.notifyRecoveryAboutToExpire(absence.getStudent(), absence.getExpirationDate());
        } catch (Exception e) {
            log.error("Error notificando recuperación próxima a vencer {}", absence.getId(), e);
        }
    }

    private List<Absence> getPendingAbsences() {
        return absenceRepository.findByStatus(AbsenceStatus.PENDING);
    }
}
