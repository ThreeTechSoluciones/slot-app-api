package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.PlanType;
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
    private PlanType planType;
    @Id
    UUID id;
}
