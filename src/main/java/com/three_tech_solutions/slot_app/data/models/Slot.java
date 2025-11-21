package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Slot {
    private DayOfWeek dayOfWeek;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private byte capacity;
    @ManyToOne
    private User user;
    @OneToMany(cascade = CascadeType.ALL)
    private List<SpecificSlot> specificSlots;
    @Id
    private UUID id;
}
