package com.chaydson.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import com.chaydson.entity.Product;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public Product findByCode(String code) {
        return find("code", code).firstResult();
    }

    public List<Product> findAllOrderedByPriceDesc() {
        return list("ORDER BY price DESC");
    }
}
