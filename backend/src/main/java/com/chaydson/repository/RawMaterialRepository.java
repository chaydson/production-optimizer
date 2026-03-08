package com.chaydson.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import com.chaydson.entity.RawMaterial;

@ApplicationScoped
public class RawMaterialRepository implements PanacheRepository<RawMaterial> {
    
    public RawMaterial findByCode(String code) {
        return find("code", code).firstResult();
    }
}
