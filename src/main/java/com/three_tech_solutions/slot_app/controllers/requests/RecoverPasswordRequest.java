package com.three_tech_solutions.slot_app.controllers.requests;

import com.three_tech_solutions.slot_app.validations.annotations.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@PasswordMatches
public record RecoverPasswordRequest(
        @NotBlank(message = "Debe ingresar el nombre de usuario")
        String username,
        @NotBlank(message = "Debe ingresar la contraseña")
        String password,
        @NotBlank(message = "Debe repetir la contraseña")
        String repeatedPassword,
        @NotNull(message = "El token es obligatorio")
        String token
) {
}
