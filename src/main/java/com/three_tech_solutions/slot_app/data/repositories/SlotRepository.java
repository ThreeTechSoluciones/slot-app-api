package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public interface SlotRepository extends JpaRepository<Slot, UUID> {
    @Query("""
        SELECT COUNT(s) > 0
        FROM Slot s
        WHERE :dayOfWeek = s.dayOfWeek
        AND :time >= s.startTime
        AND :time <  s.endTime
    """)
    boolean existsWithinRange(@Param("time") LocalTime time, @Param("dayOfWeek") DayOfWeek dayOfWeek);
}
