package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Slot {
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private byte capacity;
    @ManyToOne
    private User user;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "slot_id")
    private List<SpecificSlot> specificSlots;
    @ManyToMany
    private Set<Student> students = new HashSet<>();
    @Id
    private UUID id = UUID.randomUUID();

    public Slot(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, byte capacity, User user, List<SpecificSlot> specificSlots) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.user = user;
        this.specificSlots = specificSlots;
    }
}
