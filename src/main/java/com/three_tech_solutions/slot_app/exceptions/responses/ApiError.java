package com.three_tech_solutions.slot_app.exceptions.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ApiError(
        int status,
        List<String> errors,
        String path,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDateTime timestamp
) {
}
