package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.AbsenceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Absence {
    private LocalDate slotDate;
    @Enumerated(EnumType.STRING)
    private AbsenceStatus status;
    private LocalTime startTime;
    private LocalTime endTime;
    @ManyToOne
    private Student student;
    @Id
    private UUID id;
}
