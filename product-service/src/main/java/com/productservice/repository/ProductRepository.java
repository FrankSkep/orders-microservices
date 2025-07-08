package com.productservice.repository;

import com.productservice.dto.ProductResponse;
import com.productservice.entity.Category;
import com.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<ProductResponse> findAllByCategory(Category category);

    @Query("SELECT new com.productservice.dto.ProductResponse(p.id, p.name, p.description, p.price, p.stock, c.name, p.createdAt) " +
            "FROM Product p LEFT JOIN p.category c")
    List<ProductResponse> findAllProjected();

    @Query("SELECT new com.productservice.dto.ProductResponse(p.id, p.name, p.description, p.price, p.stock, c.name, p.createdAt) " +
            "FROM Product p LEFT JOIN p.category c WHERE p.id = :id")
    Optional<ProductResponse> findProductById(Long id);
}
