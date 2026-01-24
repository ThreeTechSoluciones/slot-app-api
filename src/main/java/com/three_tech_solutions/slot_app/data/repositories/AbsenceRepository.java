package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.enums.AbsenceStatus;
import com.three_tech_solutions.slot_app.data.models.Absence;
import com.three_tech_solutions.slot_app.data.models.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AbsenceRepository extends JpaRepository<Absence, UUID> {

    List<Absence> findByStatusAndSlotDateBefore(
            AbsenceStatus status,
            LocalDate date
    );
}
