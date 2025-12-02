package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.SpecificSlotDetailStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpecificSlotDetail {
    @OneToOne
    private Student student;
    @Enumerated(EnumType.STRING)
    private SpecificSlotDetailStatus status = SpecificSlotDetailStatus.ATTENDANCE;
    @Id
    private UUID id = UUID.randomUUID();
}
