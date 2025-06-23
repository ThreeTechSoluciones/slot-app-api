package com.three_tech_solutions.slot_app.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
public class StudentRequestDTO {
    //El nombre no puede estar vacío
    @NotBlank
    private String name;

    //El apellido no puede estar vacío
    @NotBlank
    private String lastName;

    //El numero de teléfono no puede estar vacío y debe tener un formato de numero de teléfono.
    @NotBlank
    @Pattern(regexp = "\\d{13}") //13 digitos numéricos, sin espacios
    private String cellphoneNumber;

    //El tipo de pago no puede ser nulo
    @NotNull
    private UUID paymentType; //porque es el ID de la clase

    //Las clases por semana tiene que ser mayor a cero y menor o igual a 7
    @NotNull
    @Min(1)
    @Max(7)
    private short classesPerWeek;

    private short paymentDay;

    @NotNull
    private LocalDate birthday;

    private LocalDate admissionDate;

    //patologías no puede estar vacío
    @NotBlank
    private String pathologies;

    //El userId no puede ser nulo
    @NotNull
    private UUID userId;
}
