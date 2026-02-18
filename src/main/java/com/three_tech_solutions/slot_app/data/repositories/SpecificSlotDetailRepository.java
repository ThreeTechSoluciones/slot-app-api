package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.SpecificSlotDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public interface SpecificSlotDetailRepository extends JpaRepository<SpecificSlotDetail, UUID> {
    Optional<SpecificSlotDetail> findBySpecificSlotIdAndStudentId(UUID specificSlotId, UUID studentId);

    @Modifying
    @Query("""
        DELETE FROM SpecificSlotDetail ssd
        WHERE ssd.student.id = :studentId
        AND (
            ssd.specificSlot.slotDate > :today
            OR (
                ssd.specificSlot.slotDate = :today
                AND ssd.specificSlot.startTime > :now
            )
        )
    """)
    void deleteFutureByStudent(
            @Param("studentId") UUID studentId,
            @Param("today") LocalDate today,
            @Param("now") LocalTime now
    );
}
