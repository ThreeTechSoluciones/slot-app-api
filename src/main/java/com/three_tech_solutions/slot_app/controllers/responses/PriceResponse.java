package com.three_tech_solutions.slot_app.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PriceResponse {
    private UUID id;
    private String name;
    private double amount;
}
