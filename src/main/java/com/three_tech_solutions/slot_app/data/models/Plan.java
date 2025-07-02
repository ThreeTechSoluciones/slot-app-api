package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Plan {
    private byte classesPerWeek;
    private Byte paymentDay; //porque es opcional
    @Id
    UUID id;

    @ManyToOne
    private PlanType paymentType;
}
