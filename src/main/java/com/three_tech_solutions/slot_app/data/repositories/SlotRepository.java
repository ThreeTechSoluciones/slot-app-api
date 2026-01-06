package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.Slot;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface SlotRepository extends JpaRepository<Slot, UUID> {
    @Query("""
        SELECT COUNT(s) > 0
        FROM Slot s
        WHERE :dayOfWeek = s.dayOfWeek
          AND :time >= s.startTime
          AND :time <  s.endTime
          AND (:excludedSlotId IS NULL OR s.id <> :excludedSlotId)
    """)
    boolean existsWithinRange(
            @Param("time") LocalTime time,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("excludedSlotId") UUID excludedSlotId
    );

    List<Slot> findAllByUserAndDayOfWeekAndActiveTrueOrderByStartTimeAsc(User user, DayOfWeek dayOfWeek);

    List<Slot> findAllByUserAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(User user);
}
