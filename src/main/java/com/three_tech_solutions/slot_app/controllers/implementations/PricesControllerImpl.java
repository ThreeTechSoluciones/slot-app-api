package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.PricesController;
import com.three_tech_solutions.slot_app.services.interfaces.PricesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class PricesControllerImpl implements PricesController {

    private final PricesService pricesService;

    public void deletePrice(UUID priceId) {
        pricesService.deletePrice(priceId);
    }
}
