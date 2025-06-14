package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class PaymentType {
    String name;
    String description;
    @Id
    UUID id;
}
