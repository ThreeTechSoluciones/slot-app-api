package com.three_tech_solutions.slot_app.controllers.requests;

import com.three_tech_solutions.slot_app.data.enums.PlanType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpdateStudentRequest {
    @NotBlank(message="Nombre es obligatorio")
    @NotNull(message ="Nombre no puede ser nulo")
private String name;

    @NotBlank(message="Apellido es obligatorio")
    private String lastName;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 8, message = "El DNI no puede tener más de 8 números")
    @Pattern(regexp = "\\d+", message = "El DNI debe contener solo números")
    private String dni;

    @NotBlank (message="Teléfono es obligatorio")
    @Pattern(regexp = "\\d{9,15}" , message ="Teléfono debe tener entre 9 y 15 dígitos" )//minimo 9 digitos numéricos y maximo 15, sin espacios
    private String cellphoneNumber;

    @NotNull( message = "El tipo de plan es obligatorio")
    private PlanType planType;

    @NotNull (message = "Clases por semana es obligatorio")
    @Min(1)
    @Max(7)

    private byte classesPerWeek;

    private Byte extraClasses;

    private Byte paymentDay;

    @NotNull(message="Cumpleaños es obligatorio")
    private LocalDate birthday;

    private LocalDate admissionDate = LocalDate.now();

    @Size(min = 1)
    private String pathologies;


}


