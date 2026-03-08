package com.chaydson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

public class RawMaterialDTO {

    public record Request(
        @NotBlank(message = "Code is required and cannot be blank") 
        String code,

        @NotBlank(message = "Name is required and cannot be blank") 
        String name,

        @NotNull(message = "Stock quantity is required") 
        @PositiveOrZero(message = "Stock quantity cannot be negative") 
        BigDecimal stockQuantity
    ) {}

    public record Response(
        UUID id,
        String code,
        String name,
        BigDecimal stockQuantity
    ) {}
}