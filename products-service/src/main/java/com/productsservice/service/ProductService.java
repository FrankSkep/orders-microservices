package com.productsservice.service;

import com.productsservice.dto.ProductResponse;
import com.productsservice.exception.ProductNotFoundException;
import com.productsservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAllProjected();
    }

    public ProductResponse getProductById(Long id) {
        return productRepository.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }
}
