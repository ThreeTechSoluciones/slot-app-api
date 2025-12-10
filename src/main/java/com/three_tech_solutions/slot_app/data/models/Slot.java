package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

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
    private List<SpecificSlot> specificSlots;
    @ManyToMany
    @JoinTable(
            name = "slot_student",
            joinColumns = @JoinColumn(name = "slot_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;
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
