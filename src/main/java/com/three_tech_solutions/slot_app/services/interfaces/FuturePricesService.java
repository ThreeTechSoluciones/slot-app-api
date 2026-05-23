package com.three_tech_solutions.slot_app.services.interfaces;

import org.springframework.stereotype.Service;

import java.util.UUID;


public interface FuturePricesService {
    public void deleteFuturePrice(UUID futurePriceId);
}
