package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.PriceResponse;
import com.three_tech_solutions.slot_app.data.models.Price;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriceMapper {
    public PriceResponse toPriceResponse(Price price) {
        return new PriceResponse(price.getId(), price.getName(), price.getAmount());
    }

    public List<PriceResponse> toPriceResponseList(List<Price> price) {
        return price.stream().map(this::toPriceResponse).toList();
    }
}
