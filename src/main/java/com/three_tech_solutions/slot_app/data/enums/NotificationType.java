package com.three_tech_solutions.slot_app.data.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    NEW_MONTHLY_FEE("Nueva cuota generada"),
    MONTHLY_FEE_EXPIRATION("Cuota vencida"),
    RESTORE_PASSWORD("Restablecimiento de contraseña"),
    SLOT_RECOVERY("Recuperación de turno"),
    MONTHLY_FEE_EXPIRING_SOON("Cuota por vencer");

    private final String subject;

    NotificationType(String subject) {
        this.subject = subject;
    }
}
