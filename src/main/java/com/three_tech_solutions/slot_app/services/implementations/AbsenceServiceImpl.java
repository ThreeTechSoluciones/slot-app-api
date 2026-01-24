package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.data.enums.AbsenceStatus;
import com.three_tech_solutions.slot_app.data.models.Absence;
import com.three_tech_solutions.slot_app.data.repositories.AbsenceRepository;
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
public class AbsenceServiceImpl {

    private final AbsenceRepository absenceRepository;

    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    public void expirePendingAbsences() {
        log.info("Iniciando proceso de expiración de ausencias");
        LocalDate expirationDate = LocalDate.now().minusDays(14);

        List<Absence> expiredAbsences =
                absenceRepository.findByStatusAndSlotDateBefore(AbsenceStatus.PENDING, expirationDate);

        expiredAbsences.forEach(absence ->
                absence.setStatus(AbsenceStatus.OUT_OF_TIME));

        absenceRepository.saveAll(expiredAbsences);
    }
}
