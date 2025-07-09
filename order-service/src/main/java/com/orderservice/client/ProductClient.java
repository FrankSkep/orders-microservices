package com.orderservice.client;

import com.orderservice.config.FeignClientConfig;
import com.orderservice.dto.OrderItemRequest;
import com.orderservice.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", configuration = FeignClientConfig.class)
public interface ProductClient {

    @GetMapping("/api/products/internal/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);

    @PostMapping("/api/products/internal/stock/validate")
    boolean validateStock(@RequestBody List<OrderItemRequest> request);

    @PostMapping("/api/products/internal/stock/decrease")
    void decreaseStock(@RequestBody List<OrderItemRequest> request);
}
