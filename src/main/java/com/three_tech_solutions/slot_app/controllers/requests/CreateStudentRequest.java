package com.three_tech_solutions.slot_app.controllers.requests;

import com.three_tech_solutions.slot_app.data.enums.PlanType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateStudentRequest {
    @NotBlank(message="Nombre no puede estar vacío")
    private String name;

    @NotBlank(message="Apellido no puede estar vacío")
    private String lastName;

    @NotBlank(message = "DNI no puede estar vacío")
    @Size(max = 8, message = "DNI no puede tener más de 8 números")
    @Pattern(regexp = "\\d+", message = "DNI debe contener solo números")
    private String dni;

    @NotBlank(message="Teléfono no puede estar vacío")
    @Pattern(regexp = "\\d{9,15}" , message ="Teléfono debe tener entre 9 y 15 dígitos" )//minimo 9 digitos numéricos y maximo 15, sin espacios
    private String cellphoneNumber;

    @NotNull(message = "El tipo de plan es obligatorio")
    private PlanType planType;

    @NotNull(message = "Clases por semana es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 clase por semana")
    @Max(value = 7, message = "No puede haber más de 7 clases por semana")
    private byte classesPerWeek;

    private Byte extraClasses;

    private Byte paymentDay;

    @NotNull (message="Cumpleaños es obligatorio")
    private LocalDate birthday;

    private LocalDate admissionDate = LocalDate.now();

    private String pathologies;

    private UUID userId;
}
