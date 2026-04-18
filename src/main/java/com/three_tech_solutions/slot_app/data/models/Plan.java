package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        LocalDate  today = LocalDate.now();
        return this.getPrices()
                .stream()
                .filter(price -> priceStartDateIsBeforeOrEqualThanToday(price, today) && priceEndDateIsNullOrIsAfterToday(price, today))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "Hubo un error al obtener los precios de los planes"));
    }

    public Optional <Price> getNextPrice (){
        Price current = getCurrentPrice();
        return this.getPrices()
                .stream()
                .filter(price -> priceIsAfterCurrentPrice(current, price))
                .min(Comparator.comparing(price -> price.startDate));
    }

    public List <Price> getFuturePrices(){
        return getNextPrice()
                .map(nextPrice -> this.getPrices()
                        .stream()
                        .filter(price -> price.startDate.isAfter(nextPrice.startDate))
                        .sorted(Comparator.comparing(price -> price.startDate))
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }


    private static boolean priceEndDateIsNullOrIsAfterToday(Price price, LocalDate today) {
        /*
            We validate endDate as null because the first price of the plan when we create it
            will not have an end date until a new price is created for that plan.
         */
        return price.endDate == null || today.isBefore(price.endDate);
    }

    private boolean priceIsAfterCurrentPrice (Price currentPrice, Price price){
        return currentPrice.startDate.isBefore(price.startDate);
    }


    private static boolean priceStartDateIsBeforeOrEqualThanToday(Price price, LocalDate today) {
        return price.startDate.isBefore(today) || price.startDate.isEqual(today);
    }
}
