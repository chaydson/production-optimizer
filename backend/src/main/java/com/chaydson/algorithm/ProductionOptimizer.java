package com.chaydson.algorithm;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.chaydson.entity.Product;
import com.chaydson.entity.ProductComposition;
import com.chaydson.entity.RawMaterial;
import com.chaydson.dto.PlanItemDTO;
import com.chaydson.dto.ProductionPlanDTO;


@ApplicationScoped
public class ProductionOptimizer {

    public ProductionPlanDTO calculateOptimalProduction(List<Product> products, List<RawMaterial> currentStock) {
        Map<UUID, BigDecimal> virtualStock = currentStock.stream()
                .collect(Collectors.toMap(RawMaterial::getId, RawMaterial::getStockQuantity));

        List<Product> sortedProducts = products.stream()
                .sorted((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()))
                .collect(Collectors.toList());

        List<PlanItemDTO> planItems = new ArrayList<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Product product : sortedProducts) {            
            int maxPossibleToProduce = Integer.MAX_VALUE;

            for (ProductComposition comp : product.getCompositions()) {
                UUID materialId = comp.getRawMaterial().getId();
                BigDecimal available = virtualStock.getOrDefault(materialId, BigDecimal.ZERO);
                BigDecimal required = comp.getRequiredQuantity();

                if (required.compareTo(BigDecimal.ZERO) > 0) {
                    int possibleWithThisMaterial = available.divide(required, 0, RoundingMode.DOWN).intValue();
                    maxPossibleToProduce = Math.min(maxPossibleToProduce, possibleWithThisMaterial);
                }
            }

            if (maxPossibleToProduce > 0 && maxPossibleToProduce != Integer.MAX_VALUE) {
                for (ProductComposition comp : product.getCompositions()) {
                    UUID materialId = comp.getRawMaterial().getId();
                    BigDecimal totalRequiredForProduction = comp.getRequiredQuantity().multiply(BigDecimal.valueOf(maxPossibleToProduce));
                    
                    BigDecimal newStockBalance = virtualStock.get(materialId).subtract(totalRequiredForProduction);
                    virtualStock.put(materialId, newStockBalance);
                }

                BigDecimal revenueFromThisProduct = product.getPrice().multiply(BigDecimal.valueOf(maxPossibleToProduce));
                totalRevenue = totalRevenue.add(revenueFromThisProduct);
                
                planItems.add(new PlanItemDTO(
                        product.getCode(),
                        product.getName(),
                        maxPossibleToProduce,
                        revenueFromThisProduct
                ));
            }
        }

        return new ProductionPlanDTO(planItems, totalRevenue);
    }
}