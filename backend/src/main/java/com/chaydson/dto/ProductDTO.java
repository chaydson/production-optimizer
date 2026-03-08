package com.chaydson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductDTO {

    public record Request(
        @NotBlank(message = "Code is required and cannot be blank")
        String code,
        
        @NotBlank(message = "Name is required and cannot be blank")
        String name,

        @NotNull(message = "Price is required") 
        @Positive(message = "Price cannot be negative") 
        BigDecimal price,
        List<CompositionDTO.Request> compositions
    ) {}

    public record Response(
        UUID id,
        String code,
        String name,
        BigDecimal price,
        List<CompositionDTO.Response> compositions
    ) {}
}