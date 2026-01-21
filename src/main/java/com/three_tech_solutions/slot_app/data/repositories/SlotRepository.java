package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.Slot;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SlotRepository extends JpaRepository<Slot, UUID> {
    @Query("""
        SELECT COUNT(s) > 0
        FROM Slot s
        WHERE s.active = true
          AND s.dayOfWeek = :dayOfWeek
          AND :startTime < s.endTime
          AND :endTime > s.startTime
          AND (:excludedSlotId IS NULL OR s.id <> :excludedSlotId)
    """)
    boolean existsActiveSlotWithinRange(
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludedSlotId") UUID excludedSlotId
    );

    @Query("""
        SELECT s
        FROM Slot s
        WHERE s.user = :user
          AND s.active = false
          AND s.dayOfWeek = :dayOfWeek
          AND s.startTime = :startTime
    """)
    Optional<Slot> findInactiveExactSlot(
            @Param("user") User user,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime
    );


    @Query("""
        SELECT s
        FROM Slot s
        WHERE s.user = :user
            AND s.active = true
            AND (:dayOfWeek IS NULL OR s.dayOfWeek = :dayOfWeek)
            ORDER BY s.dayOfWeek ASC, s.startTime ASC
    """)
    List<Slot> findAllByUserAndOptionalDayOfWeek(
            @Param("user") User user,
            @Param("dayOfWeek") DayOfWeek dayOfWeek
    );

    @Query("""
        SELECT s
        FROM Slot s
        LEFT JOIN s.students st
        WHERE st = :student
        ORDER BY s.dayOfWeek ASC, s.startTime ASC
    """)
    List<Slot> findAllByStudentOrderByDayAndTime(@Param("student") Student student);
}
