package com.three_tech_solutions.slot_app.data.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    WELCOME("Bienvenido/a"),
    NEW_MONTHLY_FEE("Nueva cuota generada"),
    MONTHLY_FEE_EXPIRATION("Cuota vencida"),
    RESTORE_PASSWORD("Restablecimiento de contraseña"),
    REGISTER_STUDENT_ABSENCE("Registro de ausencia para una clase"),
    SLOT_RECOVERY("Recuperación de turno"),
    SPECIFIC_SLOT_CANCELED("Clase cancelada"),
    MONTHLY_FEE_EXPIRING_SOON("Cuota por vencer");

    private final String subject;

    NotificationType(String subject) {
        this.subject = subject;
    }
}
