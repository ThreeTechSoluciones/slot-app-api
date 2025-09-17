package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.PriceController;
import com.three_tech_solutions.slot_app.controllers.requests.UpdatePriceRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PriceResponse;
import com.three_tech_solutions.slot_app.services.interfaces.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class PriceControllerImpl implements PriceController {
    private final PriceService priceService;
    @Override
    public PriceResponse updatePriceAmount(UUID priceId, UpdatePriceRequest request) {
        return priceService.updatePriceAmount(priceId, request);
    }
}
