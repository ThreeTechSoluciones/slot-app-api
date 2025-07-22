package com.three_tech_solutions.slot_app.exceptions.responses;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ApiError(
        int status,
        String errorMessage,
        List<String> errors,
        String path,
        LocalDateTime timestamp
) {
}
