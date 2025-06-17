package com.productsservice.controller;

import com.productsservice.entity.Product;
import com.productsservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestHeader("X-Username") String username,
                                                     @RequestHeader("X-Role") String role,
                                                     @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(productService.getAllProducts());
    }
}
