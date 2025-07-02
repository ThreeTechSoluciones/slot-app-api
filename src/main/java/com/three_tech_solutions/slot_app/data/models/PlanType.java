package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class PlanType {
    String name;
    String description;
    @Id
    UUID id;
}
