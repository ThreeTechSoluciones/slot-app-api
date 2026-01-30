package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.SpecificSlotDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpecificSlotDetailRepository extends JpaRepository<SpecificSlotDetail, UUID> {
    Optional<SpecificSlotDetail> findBySpecificSlotIdAndStudentId(UUID specificSlotId, UUID studentId);

    @Query("""
            SELECT ssd FROM SpecificSlotDetail ssd
            WHERE ssd.specificSlot.id = :specificSlotId
               AND (LOWER(ssd.student.name) LIKE LOWER(CONCAT('%', :filter, '%'))
               OR LOWER(ssd.student.lastname) LIKE LOWER(CONCAT('%', :filter, '%')))
            """)
    List<SpecificSlotDetail> findAllBySpecificSlotIdAndStudentFilter(UUID specificSlotId, String filter);
}
