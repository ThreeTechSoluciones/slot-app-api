package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.PriceUpdateRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PriceResponse;
import com.three_tech_solutions.slot_app.data.mappers.PriceMapper;
import com.three_tech_solutions.slot_app.data.models.Price;
import com.three_tech_solutions.slot_app.data.repositories.PriceRepository;
import com.three_tech_solutions.slot_app.services.interfaces.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    @Override
    public PriceResponse updatePriceAmount(UUID priceId, PriceUpdateRequest request) {
        return priceRepository.findById(priceId)
                .map(price -> {
                    price.setAmount(request.amount());
                    priceRepository.save(price);
                    return PriceMapper.toPriceUpdateResponse(price);
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Precio no encontrado"));
    }

}
