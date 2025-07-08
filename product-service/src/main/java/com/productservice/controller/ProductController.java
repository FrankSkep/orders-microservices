package com.productservice.controller;

import com.productservice.dto.ProductRequest;
import com.productservice.dto.ProductResponse;
import com.productservice.dto.ProductStockRequest;
import com.productservice.dto.ProductUpdateRequest;
import com.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Public endpoints (for all users)
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductDetails(id));
    }

    // Internal endpoints (for Feign Client of Order-Service)
    @GetMapping("/internal/{id}")
    public ResponseEntity<ProductResponse> getProductByIdInternal(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductDetails(id));
    }

    @PostMapping("/internal/stock/validate")
    public ResponseEntity<Boolean> validateStock(@RequestBody List<ProductStockRequest> request) {
        boolean valid = productService.validateStock(request);
        return ResponseEntity.ok(valid);
    }

    @PostMapping("/internal/stock/decrease")
    public ResponseEntity<Void> decreaseStockInternal(@RequestBody List<ProductStockRequest> request) {
        productService.decreaseStock(request);
        return ResponseEntity.noContent().build();
    }

    // Administrative endpoints (ADMIN only)
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> decreaseStock(@PathVariable Long id, @RequestBody Integer stock) {
        productService.discountStock(id, stock);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addProduct(@RequestBody ProductRequest productRequest) {
        productService.addProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
