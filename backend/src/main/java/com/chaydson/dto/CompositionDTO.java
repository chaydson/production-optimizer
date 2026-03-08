package com.chaydson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public class CompositionDTO {

    public record Request(
        @NotBlank(message = "Raw material code is required") 
        String rawMaterialCode,

        @NotNull(message = "Required quantity is required") 
        @Positive(message = "Quantity must be greater than zero") 
        BigDecimal requiredQuantity
    ) {}

    public record Response(
        UUID id,
        String rawMaterialCode,
        String rawMaterialName,
        BigDecimal requiredQuantity
    ) {}
}