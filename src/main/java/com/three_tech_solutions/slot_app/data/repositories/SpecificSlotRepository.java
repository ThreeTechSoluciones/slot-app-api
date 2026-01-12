package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpecificSlotRepository extends JpaRepository<SpecificSlot, UUID> {
    @Query("""
        SELECT DISTINCT s FROM SpecificSlot s
        LEFT JOIN FETCH s.specificSlotDetails d
        LEFT JOIN FETCH d.student
        WHERE s.slot.user = :user
          AND s.slotDate BETWEEN :startDate AND :endDate
        ORDER BY s.slotDate, s.startTime
        """)
    List<SpecificSlot> findAllBySlot_UserAndSlotDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
