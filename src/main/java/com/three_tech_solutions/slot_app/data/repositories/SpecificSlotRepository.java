package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.enums.SpecificSlotStatus;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpecificSlotRepository extends JpaRepository<SpecificSlot, UUID> {
    @Query("""
        SELECT DISTINCT s FROM SpecificSlot s
        LEFT JOIN FETCH s.specificSlotDetails d
        LEFT JOIN FETCH d.student
        WHERE s.user = :user
          AND s.slotDate BETWEEN :startDate AND :endDate
        ORDER BY s.slotDate, s.startTime
        """)
    List<SpecificSlot> findAllByUserAndSlotDateBetween(User user, LocalDate startDate, LocalDate endDate);

    @Query("""
        SELECT s
        FROM SpecificSlot s
        WHERE s.user = :user
          AND s.status = :status
          AND (
                s.slotDate < :today
                OR (
                    s.slotDate = :today
                    AND s.endTime < :now
                )
          )
    """)
    List<SpecificSlot> findFinishedSpecificSlotsByUser(User user, SpecificSlotStatus status, LocalDate today, LocalTime now);
}
