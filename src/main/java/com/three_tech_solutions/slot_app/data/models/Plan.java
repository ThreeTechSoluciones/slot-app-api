package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Plan {
    @Column(unique = true)
    String name;
    byte numberOfDays;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_id")
    @OrderBy("startDate DESC")
    List<Price> prices;
    @ManyToOne
    User user;
    @Id
    UUID id = UUID.randomUUID();

    public Plan(String name, byte numberOfDays, List<Price> prices, User user) {
        this.name = name;
        this.numberOfDays = numberOfDays;
        this.prices = prices;
        this.user = user;
    }
}
