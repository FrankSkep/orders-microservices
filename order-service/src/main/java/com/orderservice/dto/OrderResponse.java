package com.orderservice.dto;

import com.orderservice.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId; // id of the user who placed the order
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    //private List<OrderItem> items = new ArrayList<>();
}
