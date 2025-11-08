package com.three_tech_solutions.slot_app.data.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MonthlyFeeStatus {
    PENDING("Pendiente"),
    OUT_OF_TIME("Vencido"),
    PAYED("Pagado"),
    PAYED_OUT_OF_TIME("Pagado vencido");

    private final String value;

    MonthlyFeeStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getStatus() {
        return value;
    }
}
