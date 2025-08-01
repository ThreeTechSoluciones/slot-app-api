package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.PriceUpdateRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PriceUpdateResponse;

import java.util.UUID;

public interface PriceService {
    PriceUpdateResponse updatePriceAmount(UUID priceId, PriceUpdateRequest request);
}
