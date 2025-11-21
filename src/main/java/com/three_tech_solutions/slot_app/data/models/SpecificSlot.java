package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.SlotStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpecificSlot {
    private LocalDate slotDate;
    private byte capacity;
    private LocalTime startTime;
    private LocalTime endTime;
    private SlotStatus status;
    @OneToMany
    private List<SpecificSlotDetail> specificSlotDetails = Collections.emptyList();
    @Id
    private UUID id;
}
