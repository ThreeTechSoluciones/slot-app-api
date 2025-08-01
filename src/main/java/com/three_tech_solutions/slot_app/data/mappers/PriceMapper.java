package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.PriceUpdateResponse;
import com.three_tech_solutions.slot_app.data.models.Price;

public class PriceMapper {
    public static PriceUpdateResponse toPriceUpdateResponse(Price price) {
        return new PriceUpdateResponse(price.getId(), price.getName(), price.getAmount());

    }
}
