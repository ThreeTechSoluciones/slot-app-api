package com.three_tech_solutions.SlottApp.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Price {
    String name;
    double amount;
    @Id
    UUID id;
}
