package com.three_tech_solutions.slot_app.controllers.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

public interface PricesController {

    @DeleteMapping("/prices/{futurePriceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFuturePrice(@PathVariable UUID futurePriceId);
}
