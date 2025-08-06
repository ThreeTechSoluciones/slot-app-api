package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.PriceResponse;
import com.three_tech_solutions.slot_app.data.models.Price;

public class PriceMapper {
    public static PriceResponse toPriceUpdateResponse(Price price) {
        return new PriceResponse(price.getId(), price.getName(), price.getAmount());

    }
}
