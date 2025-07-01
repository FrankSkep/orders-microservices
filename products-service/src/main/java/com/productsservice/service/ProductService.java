package com.productsservice.service;

import com.productsservice.dto.ProductRequest;
import com.productsservice.dto.ProductResponse;
import com.productsservice.dto.ProductStockRequest;
import com.productsservice.dto.ProductUpdateRequest;
import com.productsservice.entity.Category;
import com.productsservice.entity.Product;
import com.productsservice.exception.ProductNotFoundException;
import com.productsservice.exception.ProductStockException;
import com.productsservice.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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

    public ProductResponse getProductDetails(Long id) {
        return productRepository.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    public void addProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .category(entityManager.getReference(Category.class, productRequest.getCategoryId()))
                .build();
        productRepository.save(product);
    }

    public void updateProduct(Long id, ProductUpdateRequest productRequest) {
        Product product = getProductById(id);

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(entityManager.getReference(Category.class, productRequest.getCategoryId()));

        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    public boolean validateStock(List<ProductStockRequest> requests) {
        for (ProductStockRequest req : requests) {
            var product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + req.getProductId()));
            if (product.getStock() < req.getQuantity()) {
                return false; // insufficient stock for this product
            }
        }
        return true; // all products have sufficient stock
    }

    @Transactional
    public void decreaseStock(List<ProductStockRequest> requests) {
        for (ProductStockRequest req : requests) {
            var product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + req.getProductId()));

            if (product.getStock() < req.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product ID: " + req.getProductId());
            }

            product.setStock(product.getStock() - req.getQuantity());
            productRepository.save(product);
        }
    }

    @Transactional
    public void discountStock(Long id, Integer stock) {
        Product product = getProductById(id);
        Integer currentStock = product.getStock();
        if (currentStock < stock) {
            throw new ProductStockException("Insufficient stock for product ID: " + id);
        }
        product.setStock(currentStock - stock);
    }
}
