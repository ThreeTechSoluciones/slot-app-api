package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.services.interfaces.PingService;
import org.springframework.stereotype.Service;

@Service
public class PingServiceImpl implements PingService {
    @Override
    public String ping() {
        return "pong";
    }
}
