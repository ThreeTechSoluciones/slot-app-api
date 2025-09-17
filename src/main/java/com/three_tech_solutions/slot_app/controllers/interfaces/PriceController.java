package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.UpdatePriceRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PriceResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/prices")
public interface PriceController {
    @PatchMapping("/{priceId}")
    PriceResponse updatePriceAmount(@PathVariable UUID priceId, @RequestBody @Valid UpdatePriceRequest request);
}
