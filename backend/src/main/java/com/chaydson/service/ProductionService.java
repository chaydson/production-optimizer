package com.chaydson.service;

import com.chaydson.algorithm.ProductionOptimizer;
import com.chaydson.dto.ProductionPlanDTO;
import com.chaydson.entity.Product;
import com.chaydson.entity.RawMaterial;
import com.chaydson.repository.ProductRepository;
import com.chaydson.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;


@ApplicationScoped
public class ProductionService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductionOptimizer optimizer;

    public ProductionService(ProductRepository productRepository, 
                             RawMaterialRepository rawMaterialRepository,
                             ProductionOptimizer optimizer) {
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
        this.optimizer = optimizer;
    }

    public ProductionPlanDTO calculateOptimization() {
        List<Product> allProducts = productRepository.listAll();
        List<RawMaterial> allMaterials = rawMaterialRepository.listAll();

        return optimizer.calculateOptimalProduction(allProducts, allMaterials);
    }
}