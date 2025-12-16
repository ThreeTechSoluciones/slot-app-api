package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Entity
@Data
@NoArgsConstructor
public class Plan {
    @Column(unique = true)
    String name;
    byte numberOfDays;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_id")
    @OrderBy("createdAt DESC")
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

    public Price getCurrentPrice() {
        return this.getPrices()
                .stream()
                .filter(price -> priceStartDateIsBeforeOrEqualThanToday(price, LocalDate.now()) && price.endDate == null)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "Hubo un error al obtener los precios de los planes"));
    }

    private static boolean priceStartDateIsBeforeOrEqualThanToday(Price price, LocalDate today) {
        return price.startDate.isBefore(today) || price.startDate.isEqual(today);
    }
}
