package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.PriceController;
import com.three_tech_solutions.slot_app.controllers.requests.PriceUpdateRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PriceUpdateResponse;
import com.three_tech_solutions.slot_app.services.interfaces.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class PriceControllerImpl implements PriceController {
    @Autowired
    private final PriceService priceService;
    @Override
    public PriceUpdateResponse updatePriceAmount(UUID priceId, PriceUpdateRequest request) {
        return priceService.updatePriceAmount(priceId, request);
    }
}
