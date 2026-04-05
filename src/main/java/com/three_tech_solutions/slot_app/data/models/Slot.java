package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.SpecificSlotStatus;
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
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "slot_id")
    private List<SpecificSlot> specificSlots = new ArrayList<>();
    @ManyToMany
    private Set<Student> students = new HashSet<>();
    @Id
    private UUID id = UUID.randomUUID();

    public Slot(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, byte capacity, User user) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.user = user;
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

    public List<SpecificSlot> getFutureSpecificSlots() {
        return this.specificSlots.stream()
                .filter(Slot::isFutureSpecificSlot)
                .toList();
    }

    public boolean hasAtLeastOneStudentRegisted() {
        return this.specificSlots.stream()
                .filter(Slot::isFutureSpecificSlot)
                .anyMatch(SpecificSlot::hasStudentsThatGoToSlot);
    }

    private static boolean isFutureSpecificSlot(SpecificSlot specificSlot) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        return specificSlot.getSlotDate().isAfter(today)
                || (specificSlot.getSlotDate().isEqual(today) && specificSlot.getStartTime().isAfter(now));
    }

    public boolean isAtFullCapacity() {
        return this.students.size() == this.capacity;
    }

    public boolean someSpecificSlotIsAtFullCapacity() {
        return this
                .specificSlots
                .stream()
                .anyMatch(SpecificSlot::isAtFullCapacity);
    }

    public void setCapacity(byte capacity) {
        this.capacity = capacity;
        this.getFutureSpecificSlots()
                .forEach(specificSlot -> specificSlot.setCapacity(capacity));
    }


    /**
     * Method to add specific slots from a start date to an end date with a specific time and duration.
     * It will create specific slots for each week between the start and end date.
     * @param slotDurationMinutes duration of the slot in minutes
     * @param slotCapacity capacity of the slot
     * @param user the user owner of the slot
     * @param startDate the start date of the slot creation period
     * @param endDate the end date of the slot creation period
     */
    public void addSpecificSlots(
            long slotDurationMinutes,
            byte slotCapacity,
            User user,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<SpecificSlot> newSpecificSlots = new ArrayList<>();
        LocalDate date = startDate;

        while (dateIsWithinSlotCreationPeriod(date, endDate)) {
            /*
             * This is to evict create slots with a start time
             * before now when the day of week is the same as today
             */
            if (slotTimeIsBeforeNow(startTime, date)) {
                date = date.plusWeeks(1);
                continue;
            }

            newSpecificSlots.add(buildSpecificSlot(startTime, date, slotCapacity, slotDurationMinutes, user));
            date = date.plusWeeks(1);
        }

        this.specificSlots.addAll(newSpecificSlots);
    }


    private static boolean dateIsWithinSlotCreationPeriod(LocalDate date, LocalDate endDate) {
        return date.isBefore(endDate) || date.isEqual(endDate);
    }

    private static boolean slotTimeIsBeforeNow(LocalTime startTime, LocalDate date) {
        return date.isEqual(LocalDate.now()) && startTime.isBefore(LocalTime.now());
    }

    private SpecificSlot buildSpecificSlot(
            LocalTime startTime,
            LocalDate startDate,
            byte slotCapacity,
            long slotDurationMinutes,
            User user
    ) {
        return new SpecificSlot(
                startDate,
                slotCapacity,
                startTime,
                startTime.plusMinutes(slotDurationMinutes),
                user,
                SpecificSlotStatus.CREATED
        );
    }

    public LocalDate getLastSpecificSlotDate() {
        return this.specificSlots
                .stream()
                .map(SpecificSlot::getSlotDate)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());
    }
}
