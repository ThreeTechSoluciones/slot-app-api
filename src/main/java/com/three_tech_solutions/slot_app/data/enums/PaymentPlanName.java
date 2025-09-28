package com.three_tech_solutions.slot_app.data.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentPlanName {

    SPECIFIC_DAY("Día específico", "Se vence el mismo día en cada mes y el día de pago es del 11 al 28 inclusives."),
    BEGINNING_OF_MONTH("Principio de mes", "Se paga del 1ro al 10 de cada mes.");

    final String name;
    final String description;

    PaymentPlanName(String name, String description){
        this.name = name;
        this.description = description;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

}