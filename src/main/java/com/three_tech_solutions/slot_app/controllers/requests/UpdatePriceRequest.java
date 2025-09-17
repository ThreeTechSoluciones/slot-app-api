package com.three_tech_solutions.slot_app.controllers.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record UpdatePriceRequest(
    @NotNull(message = "El monto no puede ser nulo")
    @Min(value = 0, message = "El monto debe ser mayor o igual a 0")
    Double amount,
    @NotNull(message = "Debe ingresar la fecha de vigencia del nuevo precio")
    LocalDate startDate
){}
