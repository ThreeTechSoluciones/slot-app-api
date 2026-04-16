package com.three_tech_solutions.slot_app.validations.annotations;

import com.three_tech_solutions.slot_app.validations.validators.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface PasswordMatches {

    String message() default "Las contraseñas no coinciden";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}