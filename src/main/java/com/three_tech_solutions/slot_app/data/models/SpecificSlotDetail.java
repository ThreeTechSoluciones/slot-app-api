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
    @Enumerated(EnumType.STRING)
    private SpecificSlotDetailStatus status;
    @OneToOne
    private Student student;
    @Id
    private UUID id;
}
