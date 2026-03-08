package com.chaydson.dto;

import java.math.BigDecimal;

public record PlanItemDTO(
    String productCode,
    String productName,
    int quantityToProduce,
    BigDecimal expectedRevenue
) {}
