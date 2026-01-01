package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.SpecificSlotDetailStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import static com.three_tech_solutions.slot_app.data.enums.SpecificSlotDetailStatus.ATTENDANCE;
import static com.three_tech_solutions.slot_app.data.enums.SpecificSlotDetailStatus.RECOVERED;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "specificSlot_id"}, name = "student_already_registered_in_specific_slot" )
)
public class SpecificSlotDetail {
    @ManyToOne
    private Student student;
    @ManyToOne
    private SpecificSlot specificSlot;
    @Enumerated(EnumType.STRING)
    private SpecificSlotDetailStatus status = ATTENDANCE;
    @Id
    private UUID id = UUID.randomUUID();

    public SpecificSlotDetail(Student student) {
        this.student = student;
    }

    public boolean studentGoesToSlot() {
        return this.status == ATTENDANCE || this.status == RECOVERED;
    }
}
