package com.three_tech_solutions.slot_app.validations.validators;

import com.three_tech_solutions.slot_app.controllers.requests.RecoverPasswordRequest;
import com.three_tech_solutions.slot_app.validations.annotations.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RecoverPasswordRequest> {

    @Override
    public boolean isValid(RecoverPasswordRequest value, ConstraintValidatorContext context) {

        if (value.password() == null || value.repeatedPassword() == null) {
            return true;
        }

        if (!value.password().equals(value.repeatedPassword())) {

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate("Las contraseñas no coinciden")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}