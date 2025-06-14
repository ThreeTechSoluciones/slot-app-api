package com.three_tech_solutions.SlottApp.services.implementations;

import com.three_tech_solutions.SlottApp.services.interfaces.PingService;
import org.springframework.stereotype.Service;

@Service
public class PingServiceImpl implements PingService {
    @Override
    public String ping() {
        return "pong";
    }
}
