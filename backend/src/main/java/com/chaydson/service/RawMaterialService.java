package com.chaydson.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.chaydson.dto.RawMaterialDTO;
import com.chaydson.entity.RawMaterial;
import com.chaydson.repository.RawMaterialRepository;

@ApplicationScoped
public class RawMaterialService {
    private final RawMaterialRepository repository;

    public RawMaterialService(RawMaterialRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RawMaterialDTO.Response create(RawMaterialDTO.Request request) {
        if (repository.findByCode(request.code()) != null) {
            throw new IllegalArgumentException("A raw material with code " + request.code() + " already exists.");
        }

        RawMaterial entity = new RawMaterial(
            request.code(), 
            request.name(), 
            request.stockQuantity()
        );
        
        repository.persist(entity);

        return mapToResponse(entity);
    }

    public List<RawMaterialDTO.Response> listAll() {
        return repository.listAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RawMaterialDTO.Response mapToResponse(RawMaterial entity) {
        return new RawMaterialDTO.Response(
            entity.getId(),
            entity.getCode(),
            entity.getName(),
            entity.getStockQuantity()
        );
    }
}
