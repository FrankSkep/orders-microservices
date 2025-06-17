package com.productsservice.service;

import com.productsservice.dto.ProductRequest;
import com.productsservice.dto.ProductResponse;
import com.productsservice.entity.Category;
import com.productsservice.entity.Product;
import com.productsservice.exception.ProductNotFoundException;
import com.productsservice.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAllProjected();
    }

    public ProductResponse getProductById(Long id) {
        return productRepository.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public void addProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .category(entityManager.getReference(Category.class, productRequest.getCategoryId()))
                .build();
    }
}
