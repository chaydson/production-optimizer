package com.chaydson.service;

import java.util.List;
import java.util.stream.Collectors;

import com.chaydson.dto.CompositionDTO;
import com.chaydson.dto.ProductDTO;
import com.chaydson.entity.Product;
import com.chaydson.entity.ProductComposition;
import com.chaydson.entity.RawMaterial;
import com.chaydson.repository.ProductRepository;
import com.chaydson.repository.RawMaterialRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductService {
    
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ProductService(ProductRepository productRepository, RawMaterialRepository rawMaterialRepository) {
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    @Transactional
    public ProductDTO.Response create(ProductDTO.Request request) {
        if (productRepository.findByCode(request.code()) != null) {
            throw new IllegalArgumentException("A product with code " + request.code() + " already exists.");
        }

        Product product = new Product(request.code(), request.name(), request.price());

        for (CompositionDTO.Request compRequest : request.compositions()) {
            RawMaterial rawMaterial = rawMaterialRepository.findByCode(compRequest.rawMaterialCode());
            
            if (rawMaterial == null) {
                throw new IllegalArgumentException("Raw material not found: " + compRequest.rawMaterialCode());
            }

            ProductComposition composition = new ProductComposition(
                product, 
                rawMaterial, 
                compRequest.requiredQuantity()
            );

            product.addProductComposition(composition);
        }

        productRepository.persist(product);

        return mapToResponse(product);
    }

    public List<ProductDTO.Response> listAll() {
        return productRepository.listAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProductDTO.Response mapToResponse(Product entity) {
        List<CompositionDTO.Response> compositionResponses = entity.getCompositions().stream()
                .map(this::mapCompositionToResponse)
                .collect(Collectors.toList());

        return new ProductDTO.Response(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getPrice(),
                compositionResponses
        );
    }

    private CompositionDTO.Response mapCompositionToResponse(ProductComposition composition) {
        return new CompositionDTO.Response(
                composition.getId(),
                composition.getRawMaterial().getCode(),
                composition.getRawMaterial().getName(),
                composition.getRequiredQuantity()
        );
    }  
}
