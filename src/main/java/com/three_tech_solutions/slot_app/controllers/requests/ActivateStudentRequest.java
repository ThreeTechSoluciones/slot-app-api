package com.three_tech_solutions.slot_app.controllers.requests;

import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ActivateStudentRequest(
        @NotNull(message = "El tipo de plan de pago es obligatorio")
        PaymentPlanName paymentPlanName,
        Byte paymentDay,
        Byte extraClasses,
        Double classPrice,
        @NotNull(message = "Debe ingresar un plan")
        UUID planId,
        @NotEmpty(message = "El alumno debe tener al menos un horario asignado")
        List<UUID> slotIds
) {
}
