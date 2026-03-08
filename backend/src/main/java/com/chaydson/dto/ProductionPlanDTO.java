package com.chaydson.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductionPlanDTO(
    List<PlanItemDTO> items,
    BigDecimal totalExpectedRevenue
) {}
