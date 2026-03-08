package com.chaydson.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "raw_materials")
public class RawMaterial {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    private BigDecimal stockQuantity;

    public RawMaterial() {
    }

    public RawMaterial(String code, String name, BigDecimal stockQuantity) {
        this.code = code;
        this.name = name;
        this.stockQuantity = stockQuantity;
    }
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getStockQuantity(){
        return stockQuantity;
    }

    public void setStockQuantity(BigDecimal stockQuantity){
        this.stockQuantity = stockQuantity;
    }

    public void deductStock(BigDecimal quantityToDeduct) {
        if (this.stockQuantity.compareTo(quantityToDeduct) < 0) {
            throw new IllegalArgumentException(
                "Not enough stock for raw material: " + this.name + "(code: " + this.code 
                    + ") . Available: " + this.stockQuantity
            );
        } else {
            this.stockQuantity = this.stockQuantity.subtract(quantityToDeduct);
        }
    }
}
