package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.data.enums.AbsenceStatus;
import com.three_tech_solutions.slot_app.data.models.Absence;
import com.three_tech_solutions.slot_app.data.repositories.AbsenceRepository;
import com.three_tech_solutions.slot_app.services.interfaces.AbsenceService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;

    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    @Override
    public void expirePendingAbsences() {
        log.info("Iniciando proceso de expiración de ausencias");
        LocalDate today = LocalDate.now();

        List<Absence> absencesToExpire = getPendingAbsences().stream()
                .filter(absence -> shouldExpireAbsence(absence, today))
                .peek(absence -> absence.setStatus(AbsenceStatus.OUT_OF_TIME))
                .toList();

        absenceRepository.saveAll(absencesToExpire);
    }

    private boolean shouldExpireAbsence(Absence absence, LocalDate today) {
        byte daysToRecover = absence.getStudent()
                .getUser()
                .getUserPreferences()
                .getDaysToRecoverAbsence();

        LocalDate expirationDate = today.minusDays(daysToRecover);

        return absence.getSlotDate().isBefore(expirationDate);
    }
    private List<Absence> getPendingAbsences() {
        return absenceRepository.findByStatus(AbsenceStatus.PENDING);
    }
}
