package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.PriceUpdateRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PriceResponse;

import java.util.UUID;

public interface PriceService {
    PriceResponse updatePriceAmount(UUID priceId, PriceUpdateRequest request);
}
