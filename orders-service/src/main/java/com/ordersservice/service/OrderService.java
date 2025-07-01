package com.ordersservice.service;

import com.ordersservice.dto.OrderItemRequest;
import com.ordersservice.dto.OrderResponse;
import com.ordersservice.dto.ProductDTO;
import com.ordersservice.entity.Order;
import com.ordersservice.entity.OrderItem;
import com.ordersservice.entity.OrderStatus;
import com.ordersservice.exception.OrderNotFoundException;
import com.ordersservice.feign.ProductClient;
import com.ordersservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Transactional
    public void createOrder(List<OrderItemRequest> items, Long userId) {

        // 1. Validate stock for all products
        boolean stockOk = productClient.validateStock(items);

        if (!stockOk) {
            throw new RuntimeException("Insufficient stock for at least one product");
        }

        // 2. Create the order and its items
        Order order = Order.builder()
                .userId(userId)
                .status(OrderStatus.PENDING)
                .build();

        List<OrderItem> orderItems = items.stream().map(i -> {
            // Get detailed product info for price and name if needed
            ProductDTO product = productClient.getProductById(i.getProductId());

            return OrderItem.builder()
                    .productId(product.getId())
                    .quantity(i.getQuantity())
                    .unitPrice(product.getPrice())
                    .order(order)
                    .build();
        }).toList();

        order.setItems(orderItems);
        order.setTotalAmount(
                orderItems.stream()
                        .map(oi -> oi.getUnitPrice().multiply(
                                new java.math.BigDecimal(oi.getQuantity())))
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
        );

        orderRepository.save(order);

        // 3. Decrease stock of the products (since the order was created)
        productClient.decreaseStock(items);
    }

    private Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    public OrderResponse getOrderById(Long id) {
        Order order = getById(id);

        return toEntity(order);
    }

    public void updateOrderStatus(Long id, String status) {
        Order order = getById(id);

        // Validate the new status if needed
        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        order.setStatus(newStatus);

        orderRepository.save(order);
    }

    public List<OrderResponse> getOrdersById(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::toEntity).toList();
    }

    private OrderResponse toEntity(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());
//        response.setItems(order.getItems());
        return response;
    }
}