package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.SpecificSlotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.three_tech_solutions.slot_app.data.enums.SpecificSlotDetailStatus.RECOVERED;

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
    @Enumerated(EnumType.STRING)
    @ManyToOne
    private Slot slot;
    private SpecificSlotStatus status;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "specific_slot_id")
    private List<SpecificSlotDetail> specificSlotDetails;
    @Id
    private UUID id = UUID.randomUUID();

    public SpecificSlot(LocalDate slotDate, byte capacity, LocalTime startTime, LocalTime endTime, SpecificSlotStatus status) {
        this.slotDate = slotDate;
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public void addStudent(Student student) {
        this.specificSlotDetails.add(new SpecificSlotDetail(student, RECOVERED));
    }
}
