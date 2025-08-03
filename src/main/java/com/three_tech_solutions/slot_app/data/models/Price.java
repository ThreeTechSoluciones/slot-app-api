package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Price {
    String name;
    double amount = 0;
    @Id
    UUID id = UUID.randomUUID();

    public Price(String name) {
        this.name = name;
    }
}
