package com.three_tech_solutions.SlottApp.controllers.implementations;

import com.three_tech_solutions.SlottApp.controllers.interfaces.PingController;
import com.three_tech_solutions.SlottApp.services.interfaces.PingService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingControllerImpl implements PingController {

    private final PingService pingService;

    public PingControllerImpl(PingService pingService) {
        this.pingService = pingService;
    }

    @Override
    public String ping() {
        return pingService.ping();
    }
}
