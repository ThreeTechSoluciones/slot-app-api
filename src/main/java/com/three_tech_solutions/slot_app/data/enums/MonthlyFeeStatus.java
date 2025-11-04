package com.three_tech_solutions.slot_app.data.enums;

public enum MonthlyFeeStatus {
    PENDING("Pendiente"),
    OUT_OF_TIME("Vencido"),
    PAYED("Pagado"),
    PAYED_OUT_OF_TIME("Pagado vencido");

    private final String value;

    MonthlyFeeStatus(String value) {
        this.value = value;
    }

    public String getStatus() {
        return value;
    }
}
