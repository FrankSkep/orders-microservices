package com.productsservice.repository;

import com.productsservice.dto.ProductResponse;
import com.productsservice.entity.Category;
import com.productsservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<ProductResponse> findAllByCategory(Category category);

    @Query("SELECT new com.productsservice.dto.ProductResponse(p.id, p.name, p.description, p.price, p.stock, c.name, p.createdAt) " +
            "FROM Product p LEFT JOIN p.category c")
    List<ProductResponse> findAllProjected();
}
