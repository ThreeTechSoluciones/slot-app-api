package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "slot_id")
    private List<SpecificSlot> specificSlots = new ArrayList<>();
    @ManyToMany
    private Set<Student> students = new HashSet<>();
    private boolean active = true;
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

    public void removeStudent(Student student) {
        this.students.remove(student);
        this.specificSlots
                .stream().filter(Slot::isFutureSpecificSlot)
                .forEach(specificSlot ->
                        specificSlot
                            .getSpecificSlotDetails()
                            .removeIf(detail -> detail.getStudent().equals(student))
                );
    }

    public void addStudent(Student student) {
        this.getStudents().add(student);
        this.getSpecificSlots()
                .stream()
                .filter(Slot::isFutureSpecificSlot)
                .forEach(specificSlot -> {
                    List<SpecificSlotDetail> slotDetails = specificSlot.getSpecificSlotDetails();
                    slotDetails.add(new SpecificSlotDetail(student));
                });
    }

    private static boolean isFutureSpecificSlot(SpecificSlot specificSlot) {
        LocalDate today = LocalDate.now();
        return specificSlot.getSlotDate().isEqual(today) || specificSlot.getSlotDate().isAfter(today);
    }


}
