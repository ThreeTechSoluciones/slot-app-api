package com.three_tech_solutions.slot_app.controllers.interfaces;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface FuturePricesController {

    @DeleteMapping("/futurePrice/{futurePriceId}")
    public void deleteFuturePrice (@PathVariable UUID futurePriceId);
}
