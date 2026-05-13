package com.three_tech_solutions.slot_app.data.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    NEW_MONTHLY_FEE("Nueva cuota generada"),
    MONTHLY_FEE_EXPIRATION("Cuota vencida"),
    MONTHLY_FEE_EXPIRING_SOON("Cuota por vencer"),
    RESTORE_PASSWORD("Restablecimiento de contraseña");

    private final String subject;

    NotificationType(String subject) {
        this.subject = subject;
    }
}
