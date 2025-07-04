package com.ordersservice.controller;

import com.ordersservice.dto.OrderItemRequest;
import com.ordersservice.dto.OrderResponse;
import com.ordersservice.entity.OrderStatus;
import com.ordersservice.security.AuthenticatedUserUtil;
import com.ordersservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuthenticatedUserUtil authenticatedUserUtil;

    // Public endpoints

    @PostMapping
    public void createOrder(@RequestBody List<OrderItemRequest> items) {
        Long userId = authenticatedUserUtil.getCurrentUserId();
        orderService.createOrder(items, userId);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        Long userId = authenticatedUserUtil.getCurrentUserId();
        return ResponseEntity.ok(orderService.getOrdersById(userId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Internal endpoints (for other services)

    @GetMapping("/internal/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/internal/{id}/status")
    public void updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
        orderService.updateOrderStatus(id, status.name());
    }
}
