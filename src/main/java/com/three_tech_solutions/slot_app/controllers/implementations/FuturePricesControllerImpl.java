package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.FuturePricesController;
import com.three_tech_solutions.slot_app.services.interfaces.FuturePricesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController

public class FuturePricesControllerImpl implements FuturePricesController {

    private final FuturePricesService futurePricesService;

     public void deleteFuturePrice (UUID futurePriceId){
        futurePricesService.deleteFuturePrice (futurePriceId);
}}
