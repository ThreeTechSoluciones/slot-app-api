package com.three_tech_solutions.slot_app.data.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PlanType {

    DIA_ESPECIFICO("Día específico", "Se vence el mismo día en cada mes."),
    PRINCIPIO_DE_MES("Principio de mes", "Se paga del 1ro al 10 de cada mes.");

    final String name;
    final String description;

    PlanType(String name, String description){
        this.name = name;
        this.description = description;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}