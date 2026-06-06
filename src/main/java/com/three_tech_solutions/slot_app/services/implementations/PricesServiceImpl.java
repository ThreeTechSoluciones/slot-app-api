package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.data.models.Price;
import com.three_tech_solutions.slot_app.data.repositories.PriceRepository;
import com.three_tech_solutions.slot_app.services.interfaces.PricesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class PricesServiceImpl implements PricesService {

    private final PriceRepository priceRepository;

    @Override
    public void deletePrice(UUID priceId) {

        Price price = priceRepository.findById(priceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El precio con ese Id no existe"));

        if (price.cantBeDeleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio no puede eliminarse porque no es futuro");
        }

        priceRepository.deleteById(priceId);
    }
}
