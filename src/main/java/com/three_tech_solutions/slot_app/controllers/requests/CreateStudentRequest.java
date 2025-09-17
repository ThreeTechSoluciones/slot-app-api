package com.three_tech_solutions.slot_app.controllers.requests;

import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateStudentRequest {
    @NotBlank(message="El nombre no puede estar vacío")
    private String name;
    @NotBlank(message="El apellido no puede estar vacío")
    private String lastName;
    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(max = 8, message = "El DNI no puede tener más de 8 números")
    @Pattern(regexp = "\\d+", message = "El DNI debe contener solo números")
    private String dni;
    @NotBlank (message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "\\d{10,11}" , message ="El número de teléfono debe tener entre 10 y 11 dígitos" )
    private String cellphoneNumber;
    @NotNull(message = "El tipo de pago es obligatorio")
    private PaymentPlanName paymentPlanName;
    @NotNull(message = "Debe ingresar el plan del alumno")
    private UUID planId;
    private Byte extraClasses;
    private Double classPrice;
    private Byte paymentDay;
    @NotNull(message = "La fecha de cumpleaños es obligatoria")
    private LocalDate birthday;
    private String pathologies;
    private UUID userId;
    private LocalDate admissionDate = LocalDate.now();
}
