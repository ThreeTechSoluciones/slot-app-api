package com.three_tech_solutions.slot_app.controllers.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/ping")
public interface PingController {
    @GetMapping
    String ping();
}
