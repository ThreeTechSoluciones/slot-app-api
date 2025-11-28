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

public class UpdateStudentRequest {
    @NotBlank(message = "El nombre no puede estar vacío")
    String name;
    @NotBlank(message = "El apellido no puede estar vacío")
    String lastName;
    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 8, message = "El DNI no puede tener más de 8 números")
    @Pattern(regexp = "\\d+", message = "El DNI debe contener solo números")
    String dni;
    @NotBlank (message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "\\d{10,11}" , message ="El número de teléfono debe tener entre 10 y 11 dígitos" )
    String cellphoneNumber;
    @NotNull(message = "El tipo de plan de pago es obligatorio")
    PaymentPlanName paymentPlanName;
    Byte paymentDay;
    @NotNull(message = "La fecha de cumpleaños es obligatoria")
    LocalDate birthday;
    String pathologies;
    @NotNull(message = "Debe ingresar un plan")
    UUID planId;

}
