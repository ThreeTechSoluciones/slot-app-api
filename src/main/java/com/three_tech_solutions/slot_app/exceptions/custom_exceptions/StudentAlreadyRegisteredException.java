package com.three_tech_solutions.slot_app.exceptions.custom_exceptions;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class StudentAlreadyRegisteredException extends ResponseStatusException {
    public StudentAlreadyRegisteredException() {
        super(BAD_REQUEST, "El alumno ya se encuentra registrado como ausente en el turno.");
    }
}
