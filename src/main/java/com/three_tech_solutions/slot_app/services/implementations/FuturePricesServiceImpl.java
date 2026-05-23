package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.data.models.Price;
import com.three_tech_solutions.slot_app.data.repositories.PriceRepository;
import com.three_tech_solutions.slot_app.services.interfaces.FuturePricesService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Service
public class FuturePricesServiceImpl implements FuturePricesService {

    private final PriceRepository priceRepository;

    @Transactional
    @Override
    public void deleteFuturePrice(UUID futurePriceId) {
        LocalDate today = LocalDate.now();

        Price price = priceRepository.findById(futurePriceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El precio con ese Id no existe"));

        if (!price.getStartDate().isAfter(today)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio no puede eliminarse porque no es futuro");
        }

        priceRepository.deleteById(futurePriceId);
    }
}
