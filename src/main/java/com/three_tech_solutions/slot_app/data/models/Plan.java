package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Plan {
    byte numberOfDays;
    byte expirationDay;
    @Id
    UUID id;
}
